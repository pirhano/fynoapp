package com.othmane.lamrani.fynoapp.controller;

import android.content.Context;

import com.othmane.lamrani.fynoapp.API.OperatingApiClient;
import com.othmane.lamrani.fynoapp.API.callback.LoginAPI;
import com.othmane.lamrani.fynoapp.API.callback.UserAPI;
import com.othmane.lamrani.fynoapp.API.models.UserDetails;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Lamrani on 12/12/2017.
 */

public class UserController {

    private UserAPI mUserAPI;
    private Context context;

    public UserController(){
        mUserAPI = getUserAPI();
    }

    // get the service
    public UserAPI getUserAPI(){
        if(mUserAPI == null){
            Retrofit retrofit = OperatingApiClient.getClient();

            return retrofit.create(UserAPI.class);
        }

        return mUserAPI;
    }


    // get all tracking items inside a support medium
    public Call<UserDetails> getUserDetails(){

        return mUserAPI.getUserDetails(OperatingApiClient.getHeader());
    }

    // get user informations


}
