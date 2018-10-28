package com.othmane.lamrani.fynoapp.API.callback;

import android.content.Context;

import com.othmane.lamrani.fynoapp.API.models.HomeVehicleStats;
import com.othmane.lamrani.fynoapp.API.models.LoginResponse;
import com.othmane.lamrani.fynoapp.API.models.UserDetails;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.helper.Methods;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.othmane.lamrani.fynoapp.API.OperatingApiClient.PREFIX_URL;

/**
 * Created by Lamrani on 03/10/2017.
 */

public interface LoginAPI {


    @FormUrlEncoded
    @POST(PREFIX_URL + "/mobile/login")
    Call<LoginResponse> getLoginResponse(@Field("email") String email, @Field("password") String password);



}
