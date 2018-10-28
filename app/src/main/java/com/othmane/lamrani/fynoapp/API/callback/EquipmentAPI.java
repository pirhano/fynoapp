package com.othmane.lamrani.fynoapp.API.callback;

import com.othmane.lamrani.fynoapp.API.models.Equipment;
import com.othmane.lamrani.fynoapp.API.models.HomeVehicleStats;
import com.othmane.lamrani.fynoapp.API.models.Stop;
import com.othmane.lamrani.fynoapp.API.models.TodayPath;
import com.othmane.lamrani.fynoapp.API.models.TrackingDynData;
import com.othmane.lamrani.fynoapp.API.models.Trip;
import com.othmane.lamrani.fynoapp.API.models.TripResponse;
import com.othmane.lamrani.fynoapp.API.models.TripTrackInfo;
import com.othmane.lamrani.fynoapp.API.models.VehicleSpeed;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

import static com.othmane.lamrani.fynoapp.API.OperatingApiClient.PREFIX_URL;

/**
 * Created by Lamrani on 21/09/2017.
 */

public interface EquipmentAPI {


    @GET(PREFIX_URL + "/dashboardData")
    Call<HomeVehicleStats> getUserVehiclesStats(@HeaderMap Map<String, String> headers);

    // review
    @GET(PREFIX_URL + "/dyndata/{id}")
    Call<Equipment> getAllDynData(@HeaderMap Map<String, String> headers, @Path("id") int id_support_medium);

    @GET(PREFIX_URL + "/mobile/dynData/{id}")
    Call<TrackingDynData> getTrackingItemInformations(@HeaderMap Map<String, String> headers, @Path("id") int id_tracking_item);

    @GET(PREFIX_URL + "/mobile/tracking_item/{id}/waypoints/{date}")
    Call<TodayPath> getItinerary(@HeaderMap Map<String, String> headers, @Path("id") int id_tracking_item, @Path("date") String date);

    @GET(PREFIX_URL + "/ST/{id}/{date}")
    Call<Stop> getStops(@HeaderMap Map<String, String> headers, @Path("id") int id_tracking_item, @Path("date") String date);

    @GET(PREFIX_URL + "/mobile/tracking_item/{id}/speed/{date}")
    Call<VehicleSpeed> getSpeed(@HeaderMap Map<String, String> headers, @Path("id") int id_tracking_item, @Path("date") String date);



    @GET(PREFIX_URL + "/getTripTable/{id}/{startdate}/{enddate}")
    Call<TripResponse> getTripTable(@HeaderMap Map<String, String> headers, @Path("id") int id_tracking_item, @Path("startdate") String startdate, @Path("enddate") String enddate);

    @GET(PREFIX_URL + "/getTrip/{id}")
    Call<List<TripTrackInfo>> getTrip(@HeaderMap Map<String, String> headers, @Path("id") long id);


}
