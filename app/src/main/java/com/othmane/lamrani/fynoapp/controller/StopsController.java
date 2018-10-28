package com.othmane.lamrani.fynoapp.controller;

import com.othmane.lamrani.fynoapp.API.OperatingApiClient;
import com.othmane.lamrani.fynoapp.API.callback.EquipmentAPI;
import com.othmane.lamrani.fynoapp.API.models.Stop;
import com.othmane.lamrani.fynoapp.helper.Constants;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Lamrani on 17/11/2017.
 */

public class StopsController {

    private EquipmentAPI mEquipmentAPI;

    public StopsController(){
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

    // get today path for a tracking item
    public Call<Stop> getTodayStops(int id_tracking_item){
        return mEquipmentAPI.getStops(OperatingApiClient.getHeader(), id_tracking_item, Constants.REFERENCE.TODAY);
    }

    // get today path for a tracking item
    public Call<Stop> getAllStops(int id_tracking_item, String date){
        return mEquipmentAPI.getStops(OperatingApiClient.getHeader(), id_tracking_item, date);
    }


}
