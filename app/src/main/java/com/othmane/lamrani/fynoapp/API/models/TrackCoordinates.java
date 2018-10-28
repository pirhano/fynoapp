package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lamrani on 27/11/2017.
 */

public class TrackCoordinates {

    @SerializedName("coordinates")
    @Expose
    private ArrayList<ArrayList<Double>> coordinates;

    public ArrayList<ArrayList<Double>> getCoordinates() {
        return coordinates;
    }

    public List<GeoPoint> getGeopoints(){
        List<GeoPoint> geoPoints = new ArrayList<>();

        for(int i=0; i<coordinates.size(); i++){
            GeoPoint geoPoint = new GeoPoint(coordinates.get(i).get(1), coordinates.get(i).get(0));
            geoPoints.add(geoPoint);
        }

        return geoPoints;
    }
}
