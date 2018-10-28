package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lamrani on 28/12/2017.
 */

public class VehicleSpeed {

    @SerializedName("max_speed")
    @Expose
    private Integer max_speed;

    public Integer getMax_speed() {
        return max_speed;
    }

    @SerializedName("data")
    @Expose
    private List<SpeedDetails> speedDetails;

    public List<SpeedDetails> getSpeedDetails() {
        return speedDetails;
    }

    public class SpeedDetails {
        @SerializedName("gps_speed")
        @Expose
        private int speed;

        @SerializedName("timestamp_gps")
        @Expose
        private String time;

        public int getSpeed() {
            return speed;
        }

        public String getTime() {
            return time;
        }
    }
}
