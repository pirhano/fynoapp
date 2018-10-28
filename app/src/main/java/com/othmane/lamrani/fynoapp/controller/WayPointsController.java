package com.othmane.lamrani.fynoapp.controller;

import com.othmane.lamrani.fynoapp.API.OperatingApiClient;
import com.othmane.lamrani.fynoapp.API.callback.EquipmentAPI;
import com.othmane.lamrani.fynoapp.API.models.TodayPath;
import com.othmane.lamrani.fynoapp.API.models.TripResponse;
import com.othmane.lamrani.fynoapp.API.models.TripTrackInfo;
import com.othmane.lamrani.fynoapp.API.models.VehicleSpeed;
import com.othmane.lamrani.fynoapp.helper.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Lamrani on 17/11/2017.
 */

public class WayPointsController {

    private EquipmentAPI mEquipmentAPI;

    public WayPointsController(){
        mEquipmentAPI = getEquipmentAPI();
    }

    // get the service
    public EquipmentAPI getEquipmentAPI(){
        if(mEquipmentAPI == null){
            Retrofit retrofit = OperatingApiClient.getClient();

            return retrofit.create(EquipmentAPI.class);
        }

        return mEquipmentAPI;
    }


    // get itinerary for a specific date
    public Call<TodayPath> getItinerary(int id_tracking_item, String date){
        return mEquipmentAPI.getItinerary(OperatingApiClient.getHeader(), id_tracking_item, date);
    }

    // get All trips
    public Call<TripResponse> getTrips(int id_tracking_item, String date){
        return mEquipmentAPI.getTripTable(OperatingApiClient.getHeader(), id_tracking_item, date, date);
    }

    // get Trip
    public Call<List<TripTrackInfo>> getTrip(long id_trip){
        return mEquipmentAPI.getTrip(OperatingApiClient.getHeader(), id_trip);
    }

    // get vehicle speed for a specific date
    public Call<VehicleSpeed> getSpeed(int id_tracking_item, String date){
        return mEquipmentAPI.getSpeed(OperatingApiClient.getHeader(), id_tracking_item, date);
    }

}
