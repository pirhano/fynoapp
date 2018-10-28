package com.othmane.lamrani.fynoapp.API;

import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.helper.Methods;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lamrani on 19/11/2017.
 */

public class OperatingApiClient {

    public static final String BASE_URL = Constants.HTTP.API_BASE_URL;

    public static final String PREFIX_URL = "/api"; //"fyno_tracker_laravel/public/";

    public static Map<String, String> getHeader(){

        String token = null;
        if(Methods.getUser() != null){
            token = Methods.getUser().getToken();
        }

        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + token);
        map.put("Accept", "application/json");

        return map;
    }


    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // the timeout of each request is 120 seconds
    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build();

}
