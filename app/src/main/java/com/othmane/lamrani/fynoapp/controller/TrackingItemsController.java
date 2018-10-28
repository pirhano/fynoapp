package com.othmane.lamrani.fynoapp.controller;

import com.othmane.lamrani.fynoapp.API.OperatingApiClient;
import com.othmane.lamrani.fynoapp.API.callback.EquipmentAPI;
import com.othmane.lamrani.fynoapp.API.models.Equipment;
import com.othmane.lamrani.fynoapp.API.models.HomeVehicleStats;
import com.othmane.lamrani.fynoapp.API.models.TrackingDynData;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Lamrani on 17/11/2017.
 */

public class TrackingItemsController {

    private EquipmentAPI mEquipmentAPI;

    public TrackingItemsController(){
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


    // get all tracking items inside a support medium
    public Call<Equipment> getAllTrackingItems(int id_support_medium){
        return mEquipmentAPI.getAllDynData(OperatingApiClient.getHeader(), id_support_medium);
    }

    // get all tracking items inside a support medium
    public Call<TrackingDynData> getLastDynData(int id_tracking_item){

        return mEquipmentAPI.getTrackingItemInformations(OperatingApiClient.getHeader(), id_tracking_item);
    }

    // get all stats about user vehicles
    public Call<HomeVehicleStats> getHomeVehicleStats(){
        return mEquipmentAPI.getUserVehiclesStats(OperatingApiClient.getHeader());
    }


}
