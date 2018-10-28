package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lamrani on 21/09/2017.
 */



public class EquipementData implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("gps_tracker_id")
    @Expose
    private String gps_tracker_id;

    @SerializedName("sim_card_id")
    @Expose
    private String sim_card_id;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;


    @SerializedName("tracking_dyn_data")
    @Expose
    private TrackingDynData tracking_dyn_data;


    public int getId() {
        return id;
    }

    public TrackingDynData getTracking_dyn_data() {
        return tracking_dyn_data;
    }

    public String getGps_tracker_id() {
        return gps_tracker_id;
    }

    public String getSim_card_id() {
        return sim_card_id;
    }

    public String getUpdated_at() {

        // Formatting date with full day and month name and show time up to
        if(updated_at != null){
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            Date date = null; //Calendar.getInstance().getTime();
            try {
                date = formatter.parse(updated_at);

                formatter = new SimpleDateFormat("EEEE dd/MM/yyyy, HH:mm", Locale.getDefault());

                return formatter.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return updated_at;
    }
}
