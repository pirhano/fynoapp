package com.othmane.lamrani.fynoapp.API.models;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.othmane.lamrani.fynoapp.helper.Methods;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Lamrani on 17/11/2017.
 */

public class Waypoint {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lon")
    @Expose
    private Double lon;

    @SerializedName("altitude")
    @Expose
    private int altitude;

    @SerializedName("gps_speed")
    @Expose
    private Integer gps_speed;

    @SerializedName("canbus_speed")
    @Expose
    private int canbus_speed;

    @SerializedName("course")
    @Expose
    private int course;

    @SerializedName("timestamp_gps")
    @Expose
    private String timestamp_gps;

    @SerializedName("total_vehicles")
    @Expose
    private int key_state;

    @SerializedName("engine_state")
    @Expose
    private int engine_state;

    @SerializedName("canbus_distance_counter")
    @Expose
    private int canbus_distance_counter;

    @SerializedName("gps_distance_counter")
    @Expose
    private int gps_distance_counter;

    @SerializedName("canbus_time_counter")
    @Expose
    private int canbus_time_counter;

    @SerializedName("gps_time_counter")
    @Expose
    private int gps_time_counter;

    @SerializedName("gsm_signal_strength")
    @Expose
    private int gsm_signal_strength;

    @SerializedName("gps_fuel_level")
    @Expose
    private int gps_fuel_level;

    @SerializedName("no_gps_signal")
    @Expose
    private int no_gps_signal;

    @SerializedName("move_state")
    @Expose
    private int move_state;

    @SerializedName("stop_state")
    @Expose
    private int stop_state;

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public GeoPoint getGeopoint(){
        if(lat != null && lon != null){
            return new GeoPoint(lat, lon);
        }
        return null;
    }

    public String getTimestamp_gps(Context context) {

        // Formatting date with full day and month name and show time up to
        if(timestamp_gps != null){
            return Methods.getDateTime(context, timestamp_gps);
        }

        return timestamp_gps;
    }

    public int getCourse() {
        return course;
    }

    public Integer getGps_speed() {
        return gps_speed;
    }
}
