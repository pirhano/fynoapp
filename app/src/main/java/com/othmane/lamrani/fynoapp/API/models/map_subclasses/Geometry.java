package com.othmane.lamrani.fynoapp.API.models.map_subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lamrani on 09/10/2017.
 */

public class Geometry {

    @SerializedName("type")
    @Expose
    private String type;


    public String getType() {
        return type;
    }

}
