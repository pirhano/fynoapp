package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.othmane.lamrani.fynoapp.API.models.map_subclasses.Features;

import java.util.List;

/**
 * Created by Lamrani on 09/10/2017.
 */

public class MapFeature {

    @SerializedName("features")
    @Expose
    private List<Features> features;

    public List<Features> getFeatures() {
        return features;
    }

}
