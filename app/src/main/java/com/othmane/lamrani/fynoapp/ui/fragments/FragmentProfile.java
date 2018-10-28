package com.othmane.lamrani.fynoapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.UserDetails;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.controller.UserController;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class FragmentProfile extends Fragment implements MainActivity.OnBackPressedListener{

    private TextView tv_user_first_name, tv_user_last_name, tv_user_address, tv_user_job_function, tv_user_phone, tv_user_cin;
    private ImageView iv_profile;
    private UserController userController;
    private Call<UserDetails> userCall;
    private ImageButton btn_refresh;
    private RelativeLayout progressLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(userCall != null){
            if(userCall.isExecuted()){
                userCall.cancel();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // onback pressed listener
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        configView();

        Picasso.with(getActivity()).
                load(R.drawable.profile).
                transform(new CropCircleTransformation()).
                into(iv_profile);

        userController = new UserController();
        refreshProfile();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshProfile();
            }
        });

    }

    public void showProgress(){
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        progressLayout.setVisibility(View.GONE);
    }

    /**
     * Refresh user profile from DB
     */
    private void refreshProfile() {

        showProgress();

        userCall = userController.getUserDetails();

        userCall.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                Log.i("Retrofit call", "success");

                UserDetails userDetails = response.body();

                if(userDetails != null){

                    String st_user_first_name = (userDetails.getFirst_name() != null) ? userDetails.getFirst_name() : "--";

                    String st_user_last_name = (userDetails.getName() != null) ? userDetails.getName() : "--";

                    String st_user_phone = (userDetails.getPhone_number() != null) ? userDetails.getPhone_number() : "--";

                    String st_user_cin = (userDetails.getIdentity_card_number() != null) ? userDetails.getIdentity_card_number() : "--";

                    String st_user_address = userDetails.getHouse_number() + " " + userDetails.getStreet() + " " + userDetails.getCity();

                    String st_user_job_function = (userDetails.getCurrentJob_function() != null) ? userDetails.getCurrentJob_function() : "--";

                    tv_user_first_name.setText( st_user_first_name);
                    tv_user_last_name.setText(st_user_last_name);
                    tv_user_phone.setText(st_user_phone);
                    tv_user_cin.setText(st_user_cin);
                    tv_user_job_function.setText(st_user_job_function);
                    tv_user_address.setText(st_user_address);

                    hideProgress();
                }
                else{
                    Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    hideProgress();
                }


            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.i("Retrofit call", "failure");
                hideProgress();

                if(!userCall.isCanceled()){
                    if(!Methods.isNetworkAvailable(getContext())){
                        Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
                    }
                }

            }
        });
    }

    private void configView() {
        if(getView() != null){
            tv_user_first_name = getView().findViewById(R.id.tv_user_name);
            tv_user_last_name = getView().findViewById(R.id.tv_user_last_name);
            tv_user_address = getView().findViewById(R.id.tv_user_address);
            tv_user_job_function = getView().findViewById(R.id.tv_user_job_function);
            tv_user_phone = getView().findViewById(R.id.tv_user_phone);
            tv_user_cin = getView().findViewById(R.id.tv_user_cin);
            iv_profile = getView().findViewById(R.id.iv_profile);
            btn_refresh = getView().findViewById(R.id.btn_refresh);
            progressLayout = getView().findViewById(R.id.progress);

            ImageButton btn_menu = getView().findViewById(R.id.btn_menu);
            btn_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.showMenu();
                }
            });
        }
    }

    @Override
    public void doBack() {
        ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_home, null);
    }
}
