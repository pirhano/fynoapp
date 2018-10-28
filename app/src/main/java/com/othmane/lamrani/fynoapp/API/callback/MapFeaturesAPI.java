package com.othmane.lamrani.fynoapp.API.callback;

import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.API.models.MapFeature;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Lamrani on 09/10/2017.
 */

public interface MapFeaturesAPI {

    @GET(Constants.GEOSERVER.BASE_URL)
    Call<MapFeature> getFeaturesInformations(@QueryMap(encoded = true) Map<String, String> options);

}
