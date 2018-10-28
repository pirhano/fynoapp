package com.othmane.lamrani.fynoapp.API.callback;

import com.othmane.lamrani.fynoapp.API.models.RouteResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Lamrani on 16/10/2017.
 */

public interface RoutingAPI {

    @GET("pgRouting/drawRouteTracking.php")
    Call<RouteResponse> getRouteFromAtoB(@QueryMap(encoded = true) Map<String, Double> options);

}
