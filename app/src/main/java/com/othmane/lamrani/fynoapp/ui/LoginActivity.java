package com.othmane.lamrani.fynoapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.TempUserInfo;
import com.othmane.lamrani.fynoapp.API.models.UserDetails;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.controller.LoginController;
import com.othmane.lamrani.fynoapp.controller.UserController;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.API.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText input_username;
    private EditText input_password;
    private ProgressDialog progressDialog;
    private LoginController loginController;
    private CheckBox check_remember_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the context value
        if(Methods.context == null){
            Methods.context= getApplicationContext();
        }

        // setLocal language
        Methods.setLocale(getApplicationContext());

        setContentView(R.layout.activity_login);

        configView();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                progressDialog.dismiss();
                finish();*/


                String st_username = input_username.getText().toString();
                String st_password = input_password.getText().toString();

                if(st_username.isEmpty() || st_password.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Missing username or password, please verify your credentials and retry", Toast.LENGTH_SHORT).show();
                    Methods.vibrateDevice(getApplicationContext());
                }
                else{
                    // check if the network is available
                    if(Methods.isNetworkAvailable(getApplicationContext())){
                        // show the progress dialog
                        progressDialog.show();

                        // Check if the credentials are matching
                        final Call<LoginResponse> loginResponseCall =  loginController.getLoginresponse(st_username, st_password);

                        // on dismissing the progress dialog
                        /*progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Toast.makeText(LoginActivity.this, "Connexion annullée", Toast.LENGTH_SHORT).show();
                                loginResponseCall.cancel();
                            }
                        });*/


                        loginResponseCall.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                                // get the status code
                                int statusCode = response.code();

                                Log.d("Login Activity", "Response: " + statusCode);

                                if(statusCode == Constants.HTTP.STATUS_CODE_SUCCESS){

                                    // get the body of the response
                                    LoginResponse loginResponse = response.body();

                                    if(loginResponse != null){
                                        // check if the response is not null
                                        if(!loginResponse.getError()){
                                            login_action(loginResponse);
                                        }
                                    }
                                }
                                // Internal error
                                else if(statusCode != Constants.HTTP.STATUS_CODE_NOT_AUTHORIZED){
                                    progressDialog.dismiss();
                                    Methods.vibrateDevice(getApplicationContext());
                                    Toast.makeText(getApplicationContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                                }
                                // Bad credentials
                                else{
                                        // dismiss the progress dialog
                                        progressDialog.dismiss();
                                        // set errors for each field
                                        input_username.setError(getString(R.string.bad_username));
                                        input_password.setError(getString(R.string.bad_pasword), null);

                                        // vibrate the phone
                                        Methods.vibrateDevice(getApplicationContext());

                                        // Configure the animation
                                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                                        input_username.startAnimation(shake);
                                        input_password.startAnimation(shake);

                                        Toast.makeText(LoginActivity.this, getString(R.string.bad_username_and_password), Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                Methods.vibrateDevice(getApplicationContext());
                                Toast.makeText(getApplicationContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                                Log.d("Login Activity", "Failure: " + t.getMessage() +" ==> " + call.request());
                                progressDialog.dismiss();
                            }
                        });
                    }
                    else{
                        Methods.vibrateDevice(getApplicationContext());
                        //Toast.makeText(getApplicationContext(), getString(R.string.network_disabled), Toast.LENGTH_LONG).show();
                        Methods.snackbarNetworkUnavailable(getApplicationContext(), v, null);
                    }
                }
            }
        });

    }

    private void login_action(final LoginResponse loginResponse) {
        // Log the user into the application
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        UserController userController = new UserController();

        final String token =  loginResponse.getToken();
        if(token != null) {
            // save token first
            TempUserInfo user = new TempUserInfo(0, null, null, token, check_remember_me.isChecked());
            Methods.saveUser(getApplicationContext(), user);

            Call<UserDetails> userDetails_call = userController.getUserDetails();
            userDetails_call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                    UserDetails userDetails = response.body();
                    if(userDetails != null){
                        // store the current user details
                        MainActivity.userDetails = userDetails;
                        TempUserInfo tempUserInfo = new TempUserInfo(userDetails.getId(), userDetails.getName() + " " + userDetails.getFirst_name(), userDetails.getCurrentJob_function(), token, check_remember_me.isChecked());

                        Methods.saveUser(getApplicationContext(), tempUserInfo);

                        // go to home activity
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        Methods.vibrateDevice(getApplicationContext());
                        Toast.makeText(getApplicationContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {
                    Log.i("Retrofit", "Failure load user details " + t.getMessage() );
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
        }



        // to delete

        // *******

        /*// get the token and store it in shared preferences
        String token =  loginResponse.getToken();
        User user = loginResponse.getUser();
        if(token != null){
            Methods.saveToken(getApplicationContext() ,token);
            Methods.saveUser(getApplicationContext(), user);

            intent.putExtra(Constants.REFERENCE.USER, user);
            // redirect to the main activity
            startActivity(intent);
            progressDialog.dismiss();
            finish();
        }*/
    }


    private void configView() {
        // Full screen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide action bar
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // btn to log in
        btn_login = (Button) findViewById(R.id.btn_login);
        // Input for username
        input_username = (EditText) findViewById(R.id.input_email);
        // Input for password
        input_password = (EditText) findViewById(R.id.input_password);
        // check box for remember me
        check_remember_me = (CheckBox) findViewById(R.id.check_remember_me);

        // Progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Connexion");
        progressDialog.setMessage("Connexion en cours ..");
        progressDialog.setIcon(R.drawable.ic_account_black_24dp);
        progressDialog.setCancelable(false);

        // configuring retrifit
        configRetrofitCall();

    }

    private void configRetrofitCall(){
        loginController = new LoginController();
    }



}
