package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.othmane.lamrani.fynoapp.API.models.map_subclasses.Edge;

import java.util.List;

/**
 * Created by Lamrani on 16/10/2017.
 */

public class RouteResponse {

    @SerializedName("edges")
    @Expose
    private List<Edge> edges;

    @SerializedName("total_cost")
    @Expose
    private double total_cost;

}
