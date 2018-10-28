package com.othmane.lamrani.fynoapp.API.models;

import android.app.Activity;
import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Methods;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by Lamrani on 03/12/2017.
 */

public class TripStops implements Serializable{

    public String stop_duration;

    public String stop_timestamp;

    private Double stop_lon;

    private Double stop_lat;

    private GeoPoint geoPoint;

    public String getStop_duration() {
        return stop_duration;
    }

    public void setStop_duration(String stop_duration) {
        this.stop_duration = stop_duration;
    }

    public String getStop_timestamp(Context context) {
        if(stop_timestamp != null){
            return Methods.getTimeOnly(context, stop_timestamp);
        }

        return stop_timestamp;
    }

    public void setStop_timestamp(String stop_timestamp) {
        this.stop_timestamp = stop_timestamp;
    }

    public Double getStop_lon() {
        return stop_lon;
    }

    public void setStop_lon(Double stop_lon) {
        this.stop_lon = stop_lon;
    }

    public Double getStop_lat() {
        return stop_lat;
    }

    public void setStop_lat(Double stop_lat) {
        this.stop_lat = stop_lat;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
