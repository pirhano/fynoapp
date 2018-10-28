package com.othmane.lamrani.fynoapp.API.callback;

import com.othmane.lamrani.fynoapp.API.models.UserDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

import static com.othmane.lamrani.fynoapp.API.OperatingApiClient.PREFIX_URL;

/**
 * Created by Lamrani on 26/12/2017.
 */

public interface UserAPI {

    @GET(PREFIX_URL + "/mobile/user/details")
    Call<UserDetails> getUserDetails(@HeaderMap Map<String, String> headers);

}
