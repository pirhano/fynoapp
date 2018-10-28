package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lamrani on 26/12/2017.
 */

public class Stop {

    @SerializedName("data")
    private List<StopData> data;


    public List<StopData> getData() {
        return data;
    }
}
