package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lamrani on 06/06/2018.
 */

public class TripTrackInfo {

    @SerializedName("id")
    private long id;

    @SerializedName("track")
    private TrackCoordinates track;

    @SerializedName("stop_points")
    private StopsCoordinates stop_points;

    @SerializedName("point_courses")
    private String point_courses;

    @SerializedName("stop_timestamps")
    private String stop_timestamps;

    @SerializedName("stop_durations")
    private String stop_durations;

    public long getId() {
        return id;
    }

    public TrackCoordinates getTrack() {
        return track;
    }

    public List<Double> getPoint_courses() {

        List<Double> courses = new ArrayList<>();

        String[] st_courses = point_courses.split("\\|");

        if(st_courses.length > 0){
            for(int i=0; i<st_courses.length; i++){
                /*if(!st_courses[i].equals("")){
                    courses.add(Double.valueOf(st_courses[i]));
                }*/
                courses.add(Double.valueOf(st_courses[i]));

            }
        }

        return courses;
    }

    public List<TripStops> getStops(){
        List<String> timestamps = getStopTimestamps();
        List<String> durations = getStopDurations();

        List<TripStops> tripStops = new ArrayList<>();

        for(int i=0; i<timestamps.size(); i++){

            TripStops stop = new TripStops();

            String timestamp = timestamps.get(i);
            String duration = durations.get(i);


            stop.setStop_duration(duration);
            stop.setStop_timestamp(timestamp);

            double lat = stop_points.getGeopoints().get(i).getLatitude();
            double lon = stop_points.getGeopoints().get(i).getLongitude();

            stop.setStop_lat(lat);
            stop.setStop_lon(lon);

            stop.setGeoPoint(stop_points.getGeopoints().get(i));

            tripStops.add(stop);
        }

        return tripStops;
    }


    public List<String> getStopDurations(){
        List<String> durations = new ArrayList<>();

        String[] st_courses = stop_durations.split("\\|");

        if(st_courses.length > 0){
            for(int i=0; i<st_courses.length; i++){
                durations.add(st_courses[i]);

            }
        }

        return durations;
    }

    public List<String> getStopTimestamps(){
        List<String> timestamps = new ArrayList<>();

        String[] st_courses = stop_timestamps.split("\\|");

        if(st_courses.length > 0){
            for(int i=0; i<st_courses.length; i++){
                timestamps.add(st_courses[i]);

            }
        }

        return timestamps;
    }

}
