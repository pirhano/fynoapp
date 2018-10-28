package com.othmane.lamrani.fynoapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.HomeVehicleStats;
import com.othmane.lamrani.fynoapp.API.models.TempUserInfo;
import com.othmane.lamrani.fynoapp.API.models.UserDetails;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.controller.TrackingItemsController;
import com.othmane.lamrani.fynoapp.controller.UserController;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.helper.RequestPermission;
import com.othmane.lamrani.fynoapp.ui.MainActivity;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;
import at.grabner.circleprogress.TextMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Lamrani on 31/10/2017.
 */

public class FragmentHome extends RequestPermission implements MainActivity.OnBackPressedListener{

    private CircleProgressView mCircleView_engine_on, mCircleView_sum_alerts, mCircleView_total_vehicles;
    private Button btn_equipements;
    private Button btn_settings;
    public static boolean languageChanged = false;
    private TextView tv_name;
    private TextView tv_service;
    private TrackingItemsController mTrackingItemsController;
    private Call<HomeVehicleStats> stats_call;
    private HomeVehicleStats homeVehicleStats;
    private ImageButton btn_refresh_stats;
    private UserController userController;
    private Call<UserDetails> userDetails_call;
    private boolean doubleBackToExitPressedOnce = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // setLocal language
        Methods.setLocale(getContext());

        View rootView = inflater.inflate(R.layout.fragment_home_material, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // if the language is changed we have to refresh the activity
        if(languageChanged){
            // setLocal language
            Methods.setLocale(getContext());
            refreshHomeActivity();
            languageChanged = false;
        }
    }

    /**
     *  refreshing the activity
     */
    private void refreshHomeActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().finish();
        startActivity(intent);
        //((MainActivity)getActivity()).displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // if the call is alrealy executing we are going to cancel it
        if(stats_call != null){
            if(stats_call.isExecuted()){
                stats_call.cancel();
            }
        }
        if(userDetails_call != null){
            if(userDetails_call.isExecuted()){
                userDetails_call.cancel();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // onback pressed listener
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        mTrackingItemsController = new TrackingItemsController();
        userController = new UserController();

        // configure the view
        configView();

        // refresh circles infos
        refreshEquipmentCard();
        // refresh the user informations
        refreshUserInformations();



        btn_refresh_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircleView_sum_alerts.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.transparent));
                refreshEquipmentCard();
            }
        });

        // configure buttons actions
        // My equipments on click action
        btn_equipements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), EquipmentActivity.class);
                //startActivity(intent);
                ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_equipments, null);
            }
        });


        // settings on click action
        /*btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), SettingsActivity.class);
                //startActivity(intent);
                ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_settings, null);
            }
        });*/

    }

    public void refreshEquipmentCard(){

        mCircleView_engine_on.setText(getString(R.string.loading_message));
        mCircleView_sum_alerts.setText(getString(R.string.loading_message));
        mCircleView_total_vehicles.setText(getString(R.string.loading_message));

        //value setting
        mCircleView_engine_on.setValueAnimated(0);
        mCircleView_sum_alerts.setValueAnimated(0);
        mCircleView_total_vehicles.setValueAnimated(0);

        if(Methods.isNetworkAvailable(getContext())) {

            stats_call = mTrackingItemsController.getHomeVehicleStats();

            // refresh stats informations first
            stats_call.enqueue(new Callback<HomeVehicleStats>() {
                @Override
                public void onResponse(Call<HomeVehicleStats> call, Response<HomeVehicleStats> response) {

                    homeVehicleStats = response.body();

                    if(response.body() != null){
                        // refresh circle
                        refreshCircle_engine_on();
                        refreshCircle_total_vehicles();
                        refreshCircle_sum_alerts();

                        // if there is alerts it will be blinking
                        if(homeVehicleStats.getTotal_alerts() > 0){
                            startBlinkingAnimation(mCircleView_sum_alerts);
                        }

                    }
                    else{
                        Snackbar.make(getView(), getString(R.string.intern_error), Snackbar.LENGTH_INDEFINITE).show();
                        mCircleView_engine_on.setText(getString(R.string.unavailable));
                        mCircleView_sum_alerts.setText(getString(R.string.unavailable));
                        mCircleView_total_vehicles.setText(getString(R.string.unavailable));
                    }
                }

                @Override
                public void onFailure(Call<HomeVehicleStats> call, Throwable t) {
                    if(!call.isCanceled()){
                        //Toast.makeText(getActivity(), getString(R.string.intern_error) + " \n" + t.getMessage(), Toast.LENGTH_LONG).show();
                        mCircleView_engine_on.setText(getString(R.string.unavailable));
                        mCircleView_sum_alerts.setText(getString(R.string.unavailable));
                        mCircleView_total_vehicles.setText(getString(R.string.unavailable));
                    }
                    else{
                        //Toast.makeText(getContext(), "Loading data cancelled", Toast.LENGTH_SHORT).show();
                        Log.i("Retrofit", "Cancelled");
                    }


                }
            });

        }
        else{
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
            mCircleView_engine_on.setText(getString(R.string.unavailable));
            mCircleView_sum_alerts.setText(getString(R.string.unavailable));
            mCircleView_total_vehicles.setText(getString(R.string.unavailable));
        }

    }

    /**
     * Refresh the user informations (Name and service)
     */
    public void refreshUserInformations(){
        // Get the informations about the current user
        final TempUserInfo user = Methods.getUser();

        // Home fragment
        tv_name.setText(user.getName());
        tv_service.setText(user.getJob_function());

        // Navugation drawer
        ((MainActivity)getActivity()).tv_name.setText(user.getName());
        ((MainActivity)getActivity()).tv_service.setText(user.getJob_function());

        if(Methods.isNetworkAvailable(getContext())){
            userDetails_call = userController.getUserDetails();
            userDetails_call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                    UserDetails userDetails = response.body();
                    if(userDetails != null){
                        // store the current user details
                        MainActivity.userDetails = userDetails;
                        TempUserInfo tempUserInfo = new TempUserInfo(userDetails.getId(), userDetails.getName() + " " + userDetails.getFirst_name(), userDetails.getCurrentJob_function(), user.getToken(), user.getRemember_me());
                        Methods.saveUser(getContext(), tempUserInfo);

                        // Home fragment
                        tv_name.setText(user.getName());
                        tv_service.setText(user.getJob_function());

                        // Navugation drawer
                        ((MainActivity)getActivity()).tv_name.setText(user.getName());
                        ((MainActivity)getActivity()).tv_service.setText(user.getJob_function());
                    }
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {
                    Log.i("Retrofit", "Failure load user details");
                }
            });
        }
        else{
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
        }

    }

    /**
     * Make a view blinking (show and hide)
     * @param view target view
     */
    public void startBlinkingAnimation(View view) {
        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blinking_animation);
        view.startAnimation(startAnimation);
    }

    /**
     * Refresh circle view engine on
     */
    private void refreshCircle_engine_on() {

        //value setting
        mCircleView_engine_on.setMaxValue(homeVehicleStats.getTotal_tracking_items());
        //mCircleView_engine_on.setValue(0);
        mCircleView_engine_on.setValueAnimated(homeVehicleStats.getEngine_on());


        //growing/rotating counter-clockwise
        mCircleView_engine_on.setDirection(Direction.CW);

        if(homeVehicleStats.getEngine_on() > 0){
            mCircleView_engine_on.setText(String.valueOf(homeVehicleStats.getEngine_on()));
        }
        else{
            //mCircleView_engine_on.setText(getString(R.string.none_engine_on));
            // empty set
            mCircleView_engine_on.setText("\u2205");
        }

    }

    /**
     * Refresh circle view total vehicles
     */
    private void refreshCircle_total_vehicles() {

        //value setting
        mCircleView_total_vehicles.setMaxValue(homeVehicleStats.getTotal_tracking_items());
        //mCircleView_engine_on.setValue(0);
        mCircleView_total_vehicles.setValueAnimated(homeVehicleStats.getTotal_tracking_items());


        //growing/rotating counter-clockwise
        mCircleView_total_vehicles.setDirection(Direction.CW);

        if(homeVehicleStats.getTotal_tracking_items() > 0){
            mCircleView_total_vehicles.setText(String.valueOf(homeVehicleStats.getTotal_tracking_items()));
        }
        else{
            //mCircleView_total_vehicles.setText(getString(R.string.none_engine_on));
            // empty set
            mCircleView_total_vehicles.setText("\u2205");
        }

    }

    /**
     * Refresh circle view total vehicles
     */
    private void refreshCircle_sum_alerts() {

        //value setting
        mCircleView_sum_alerts.setMaxValue(homeVehicleStats.getTotal_tracking_items());
        //mCircleView_engine_on.setValue(0);
        mCircleView_sum_alerts.setValueAnimated(homeVehicleStats.getTotal_alerts());


        //growing/rotating counter-clockwise
        mCircleView_sum_alerts.setDirection(Direction.CW);

       if(homeVehicleStats.getTotal_alerts() > 0){
           mCircleView_sum_alerts.setText(String.valueOf(homeVehicleStats.getTotal_alerts()));
       }
       else{
           mCircleView_sum_alerts.setText("");
           mCircleView_sum_alerts.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_check_38dp));
           //mCircleView_sum_alerts.setRimColor(ContextCompat.getColor(getContext(),R.color.green));
       }

    }

    /**
     * Config fragment view
     */
    private void configView() {
        if(getView() != null){

            ImageButton btn_menu = getView().findViewById(R.id.btn_menu);
            btn_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.showMenu();
                }
            });

            // user informations
            tv_name = (TextView) getView().findViewById(R.id.tv_user_name);
            tv_service = (TextView) getView().findViewById(R.id.tv_user_service);

            // stats
            btn_refresh_stats = getView().findViewById(R.id.btn_refresh_stats);

            // bnt for action
            btn_equipements = (Button) getView().findViewById(R.id.btn_equipments_home);
            //btn_settings = (Button) getView().findViewById(R.id.btn_settings_home);
            //btn_history = (Button) getView().findViewById(R.id.btn_history_home);

            // circle view engine_on
            mCircleView_engine_on = (CircleProgressView) getView().findViewById(R.id.circleView_vehicles_engine_on);
            mCircleView_engine_on.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(float value) {
                    Log.d("Circle progress", "Progress Changed: " + value);
                }
            });
            mCircleView_engine_on.setAutoTextSize(true); // enable auto text size, previous values are overwritten
            mCircleView_engine_on.setTextMode(TextMode.TEXT);
            mCircleView_engine_on.setText(getString(R.string.loading_message));

            // circle view engine_on
            mCircleView_total_vehicles = (CircleProgressView) getView().findViewById(R.id.circleView_tolal_vehicles);
            mCircleView_total_vehicles.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(float value) {
                    Log.d("Circle progress", "Progress Changed: " + value);
                }
            });
            mCircleView_total_vehicles.setAutoTextSize(true); // enable auto text size, previous values are overwritten
            mCircleView_total_vehicles.setTextMode(TextMode.TEXT);
            mCircleView_total_vehicles.setText(getString(R.string.loading_message));

            // circle view engine_on
            mCircleView_sum_alerts = (CircleProgressView) getView().findViewById(R.id.circleView_sum_alerts);
            mCircleView_sum_alerts.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(float value) {
                    Log.d("Circle progress", "Progress Changed: " + value);
                }
            });
            mCircleView_sum_alerts.setAutoTextSize(true); // enable auto text size, previous values are overwritten
            mCircleView_sum_alerts.setTextMode(TextMode.TEXT);
            mCircleView_sum_alerts.setText(getString(R.string.loading_message));
        }
    }

    @Override
    public void doBack() {
        getActivity().finish();
        System.exit(0);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }


}
