package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lamrani on 06/06/2018.
 */

public class TripResponse {

    @SerializedName("data")
    private List<Trip> trips;

    public List<Trip> getTrips() {
        return trips;
    }
}
