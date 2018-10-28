package com.othmane.lamrani.fynoapp.controller;

import com.othmane.lamrani.fynoapp.API.callback.LoginAPI;
import com.othmane.lamrani.fynoapp.API.OperatingApiClient;
import com.othmane.lamrani.fynoapp.API.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Lamrani on 15/11/2017.
 */

public class LoginController {

    private LoginAPI mLoginAPI;

    public LoginController() {
        mLoginAPI = getEquipmentAPI();
    }

    // get the service
    public LoginAPI getEquipmentAPI(){
        if(mLoginAPI == null){
            Retrofit retrofit = OperatingApiClient.getClient();

            return retrofit.create(LoginAPI.class);
        }

        return mLoginAPI;
    }

    // get the login response
    public Call<LoginResponse> getLoginresponse(String email, String password){
        return  mLoginAPI.getLoginResponse(email, password);
    }


}
