package com.othmane.lamrani.fynoapp.API.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Methods;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by Lamrani on 03/12/2017.
 */

public class StopData implements Serializable{

    @SerializedName("id")
    private Integer id;

    @SerializedName("tracking_item_id")
    private Integer tracking_item_id;

    @SerializedName("stop_timestamp_srv")
    private String stop_timestamp_srv;

    @SerializedName("no_stop_timestamp_srv")
    private String no_stop_timestamp_srv;


    @SerializedName("total_stop_duration_seconds")
    public Integer total_stop_duration_seconds;

    @SerializedName("stop_duration_minutes")
    private Integer stop_duration_minutes;

    @SerializedName("stop_duration_hours")
    private Integer stop_duration_hours;

    @SerializedName("stop_duration_days")
    private Integer stop_duration_days;


    @SerializedName("stop_lon")
    private Double stop_lon;

    @SerializedName("stop_lat")
    private Double stop_lat;

    @SerializedName("today_distance_counter")
    private Double today_distance_counter;

    @SerializedName("distance_counter")
    private Double distance_counter;

    @SerializedName("time_counter")
    private String time_counter;

    @SerializedName("today_time_counter")
    private String today_time_counter;


    public Integer getId() {
        return id;
    }

    public Integer getTracking_item_id() {
        return tracking_item_id;
    }

    public String getStop_timestamp_srv_DateTime(Context context) {

        if(stop_timestamp_srv != null){
            return Methods.getDateTime(context, stop_timestamp_srv);
        }

        return stop_timestamp_srv;
    }

    public String getStop_timestamp_srv_TimeOnly(Context context) {

        if(stop_timestamp_srv != null){
            return Methods.getTimeOnly(context, stop_timestamp_srv);
        }

        return stop_timestamp_srv;
    }


    public String getNo_stop_timestamp_srv_DateTime(Context context) {

        if(no_stop_timestamp_srv == null){
            return context.getString(R.string.still_stopped);
        }

        if(no_stop_timestamp_srv != null){
            return Methods.getDateTime(context, no_stop_timestamp_srv);
        }

        return no_stop_timestamp_srv;
    }

    public String getNo_stop_timestamp_srv_TimeOnly(Context context) {

        if(no_stop_timestamp_srv == null){
            return context.getString(R.string.still_stopped);
        }

        if(no_stop_timestamp_srv != null){
            return Methods.getTimeOnly(context, no_stop_timestamp_srv);
        }

        return no_stop_timestamp_srv;
    }

    public Double getStop_lon() {
        return stop_lon;
    }

    public Double getStop_lat() {
        return stop_lat;
    }

    public Double getDistance_counter() {
        return distance_counter;
    }


    public String getTime_counter() {
        return time_counter;
    }

    public String getToday_time_counter() {
        return today_time_counter;
    }

    public Double getToday_distance_counter() {
        return today_distance_counter;
    }

    public GeoPoint getGeopoint(){
        if(stop_lat != null && stop_lon != null){
            return new GeoPoint(stop_lat, stop_lon);
        }
        return null;
    }

    public String getStop_duration(Context context) {

        String st_duration = "";

        if(stop_duration_days > 0){
            st_duration += String.valueOf(stop_duration_days) + " " + context.getString(R.string.day);
        }
        if(stop_duration_hours > 0){
            st_duration += " " + String.valueOf(stop_duration_hours) + " H";
        }
        if(stop_duration_minutes > 0){
            st_duration += " " + String.valueOf(stop_duration_minutes) + " min";
        }
        if(total_stop_duration_seconds < 60){
            st_duration = String.valueOf(total_stop_duration_seconds) + " s";
        }

        if(st_duration.equals("")){
            st_duration = context.getString(R.string.unavailable);
        }

        return st_duration;
    }
}
