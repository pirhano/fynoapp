package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lamrani on 15/11/2017.
 */

public class VehicleInformations {

    @SerializedName("total_vehicles")
    @Expose
    private int total_vehicles;

    @SerializedName("total_engine_on")
    @Expose
    private int total_engine_on;

    @SerializedName("total_alerts")
    @Expose
    private int total_gps_unavailable;

    @SerializedName("total_gps_unavailable")
    @Expose
    private int total_alerts;

    public VehicleInformations(int total_vehicles, int total_engine_on, int total_gps_unavailable, int total_alerts) {
        this.total_vehicles = total_vehicles;
        this.total_engine_on = total_engine_on;
        this.total_gps_unavailable = total_gps_unavailable;
        this.total_alerts = total_alerts;
    }

    public int getTotal_vehicles() {
        return total_vehicles;
    }

    public int getTotal_engine_on() {
        return total_engine_on;
    }

    public int getTotal_alerts() {
        return total_alerts;
    }

    public int getTotal_gps_unavailable() {
        return total_gps_unavailable;
    }
}
