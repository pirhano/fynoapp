package com.othmane.lamrani.fynoapp.API.models.map_subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lamrani on 16/10/2017.
 */

public class Edge {

    @SerializedName("myheading")
    @Expose
    private double myheading;

    @SerializedName("geom")
    @Expose
    private List<List<Double>> geom;

    @SerializedName("label")
    @Expose
    private double label;

}
