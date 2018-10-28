package com.othmane.lamrani.fynoapp.API.models;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.othmane.lamrani.fynoapp.helper.Methods;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by Lamrani on 17/11/2017.
 */

public class TrackingDynData implements Serializable{

    @SerializedName("tracking_item_id")
    @Expose
    private Integer tracking_item_id;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("driver")
    @Expose
    private String driver;

    @SerializedName("licence_plate")
    @Expose
    private String licence_plate;

    @SerializedName("key_state_since")
    @Expose
    private String key_state_since;

    @SerializedName("stop_state_since")
    @Expose
    private String stop_state_since;

    @SerializedName("no_gps_signal_since")
    @Expose
    private String no_gps_signal_since;

    @SerializedName("last_gps_today_distance_counter")
    @Expose
    private Integer last_gps_today_distance_counter;

    @SerializedName("today_max_speed")
    @Expose
    private Integer today_max_speed;

    @SerializedName("today_first_key_on")
    @Expose
    private String today_first_key_on;

    @SerializedName("today_first_engine_on")
    @Expose
    private String today_first_engine_on;

    @SerializedName("today_first_activity")
    @Expose
    private String today_first_activity;

    @SerializedName("last_lon")
    @Expose
    private Double last_lon;

    @SerializedName("last_lat")
    @Expose
    private Double last_lat;

    @SerializedName("last_key_state")
    @Expose
    private int last_key_state;

    @SerializedName("last_stop_state")
    @Expose
    private int last_stop_state;

    @SerializedName("last_engine_state")
    @Expose
    private int last_engine_state;

    @SerializedName("last_move_state")
    @Expose
    private int last_move_state;

    @SerializedName("move_state_since")
    @Expose
    private String move_state_since;

    @SerializedName("last_timestamp_cs")
    @Expose
    private String last_timestamp_cs;

    @SerializedName("last_gps_distance_counter")
    @Expose
    private Double last_gps_distance_counter;

    @SerializedName("last_gps_time_counter")
    @Expose
    private Double last_gps_time_counter;

    @SerializedName("last_canbus_distance_counter")
    @Expose
    private Double last_canbus_distance_counter;

    @SerializedName("last_canbus_time_counter")
    @Expose
    private Double last_canbus_time_counter;

    @SerializedName("last_gps_today_time_counter")
    @Expose
    private Double last_gps_today_time_counter;

    @SerializedName("last_canbus_today_distance_counter")
    @Expose
    private Double last_canbus_today_distance_counter;

    @SerializedName("last_canbus_today_time_counter")
    @Expose
    private Double last_canbus_today_time_counter;

    @SerializedName("last_gps_speed")
    @Expose
    private Integer last_gps_speed;

    @SerializedName("last_course")
    @Expose
    private Integer last_course;

    @SerializedName("last_distance_counter")
    @Expose
    private Double last_distance_counter;

    @SerializedName("last_time_counter")
    @Expose
    private Double last_time_counter;

    @SerializedName("last_today_time_counter")
    @Expose
    private Double last_today_time_counter;

    @SerializedName("last_today_distance_counter")
    @Expose
    private Double last_today_distance_counter;



    public Integer getTracking_item_id() {
        return tracking_item_id;
    }

    public String getKey_state_since(Context context) {

        // Formatting date with full day and month name and show time up to
        if(key_state_since != null){
            return Methods.getDateTime(context, key_state_since);
        }

        return key_state_since;
    }

    public String getStop_state_since(Context context) {

        // Formatting date with full day and month name and show time up to
        if(stop_state_since != null){
            return Methods.getDateTime(context, stop_state_since);
        }

        return stop_state_since;
    }

    public String getDriver() {
        return driver;
    }

    public String getNo_gps_signal_since(Context context) {

        // Formatting date with full day and month name and show time up to
        if(no_gps_signal_since != null){
            return Methods.getDateTime(context, no_gps_signal_since);
        }

        return no_gps_signal_since;
    }

    public Integer getLast_gps_today_distance_counter() {
        return last_gps_today_distance_counter;
    }

    public Integer getToday_max_speed() {
        return today_max_speed;
    }

    public String getToday_first_key_on(Context context) {

        // Formatting date with full day and month name and show time up to
        if(today_first_key_on != null){
            return Methods.getDateTime(context, today_first_key_on);
        }

        return today_first_key_on;
    }

    public String getToday_first_engine_on(Context context) {

        // Formatting date with full day and month name and show time up to
        if(today_first_engine_on != null){
            return Methods.getDateTime(context, today_first_engine_on);
        }

        return today_first_engine_on;
    }

    public String getToday_first_activity_DateTime(Context context) {

        // Formatting date with full day and month name and show time up to
        if(today_first_activity != null){
            return Methods.getDateTime(context, today_first_activity);
        }

        return today_first_activity;
    }

    public String getToday_first_activity_Time(Context context) {

        // Formatting date with full day and month name and show time up to
        if(today_first_activity != null){
            return Methods.getTimeOnly(context, today_first_activity);
        }

        return today_first_activity;
    }

    public Double getLast_lon() {
        return Double.valueOf(last_lon);
    }

    public Double getLast_lat() {
        return Double.valueOf(last_lat);
    }

    public int getLast_key_state() {
        return last_key_state;
    }

    public int getLast_stop_state() {
        return last_stop_state;
    }

    public int getLast_engine_state() {
        return last_engine_state;
    }

    public int getLast_move_state() {
        return last_move_state;
    }

    public String getMove_state_since(Context context) {

        // Formatting date with full day and month name and show time up to
        if(move_state_since != null){
            return Methods.getDateTime(context, move_state_since);
        }

        return move_state_since;
    }

    public String getLast_timestamp_cs_DateTime(Context context) {

        // Formatting date with full day and month name and show time up to
        if(last_timestamp_cs != null){
            return Methods.getDateTime(context, last_timestamp_cs);
        }

        return last_timestamp_cs;
    }

    public boolean isTodaySignal(){

        if(last_timestamp_cs != null){
            return Methods.isToday(last_timestamp_cs);
        }

        return false;
    }


    public String getLast_timestamp_cs_TimeOnly(Context context) {

        // Formatting date with full day and month name and show time up to
        if(last_timestamp_cs != null){
            return Methods.getTimeOnly(context, last_timestamp_cs);
        }

        return last_timestamp_cs;
    }

    public Double getLast_gps_distance_counter() {
        return last_gps_distance_counter;
    }

    public Double getLast_gps_time_counter() {
        return last_gps_time_counter;
    }

    public Double getLast_canbus_distance_counter() {
        return last_canbus_distance_counter;
    }

    public Double getLast_canbus_time_counter() {
        return last_canbus_time_counter;
    }

    public Double getLast_gps_today_time_counter() {
        return last_gps_today_time_counter;
    }

    public Double getLast_canbus_today_distance_counter() {
        return last_canbus_today_distance_counter;
    }

    public Double getLast_canbus_today_time_counter() {
        return last_canbus_today_time_counter;
    }

    public Integer getLast_gps_speed() {
        return last_gps_speed;
    }

    public Integer getLast_course() {
        return last_course;
    }

    public String getLabel() {
        return label;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public GeoPoint getGeopoint(){
        if(last_lat != null && last_lon != null){
            return new GeoPoint(last_lat, last_lon);
        }
        return null;
    }

    public Integer getLast_distance_counter() {
        if(last_distance_counter != null){
            return last_distance_counter.intValue()/1000;
        }
        return null;
    }

    public Integer getLast_time_counter() {
        if(last_time_counter != null){
            return last_time_counter.intValue()/1000;
        }
        return null;
    }

    public Integer getLast_today_time_counter() {
        if(last_today_time_counter != null){
            return last_today_time_counter.intValue()/1000;
        }
        return null;
    }

    public Integer getLast_today_distance_counter() {
        if(last_today_distance_counter != null){
            return last_today_distance_counter.intValue()/1000;
        }
        return null;
    }
}
