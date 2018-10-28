package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lamrani on 26/12/2017.
 */

public class Equipment {

    @SerializedName("data")
    private List<EquipementData> data;

    public List<EquipementData> getData() {
        return data;
    }
}
