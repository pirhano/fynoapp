package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lamrani on 06/06/2018.
 */

public class Trip {

    @SerializedName("id")
    private long id;

    @SerializedName("start_timestamp_srv")
    private String start_timestamp_srv;

    @SerializedName("end_timstamp_srv")
    private String end_timstamp_srv;

    @SerializedName("diff_distance_counter")
    private Double diff_distance_counter;

    @SerializedName("max_speed")
    private String max_speed;


    public long getId() {
        return id;
    }

    public String getStart_timestamp_srv() {
        return start_timestamp_srv;
    }

    public String getEnd_timstamp_srv() {
        return end_timstamp_srv;
    }

    public Double getDiff_distance_counter() {
        return diff_distance_counter;
    }

    public String getMax_speed() {
        return max_speed;
    }


}
