package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lamrani on 27/11/2017.
 */

public class TodayPath {

    @SerializedName("last_today_distance_counter")
    @Expose
    private Double last_today_distance_counter;

    @SerializedName("waypoints")
    @Expose
    private List<Waypoint> waypoints;

    public Integer getLast_today_distance_counter() {
        if(last_today_distance_counter != null){
            return last_today_distance_counter.intValue()/1000;
        }
        return null;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }
}
