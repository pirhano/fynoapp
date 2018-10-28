package com.othmane.lamrani.fynoapp.API.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.othmane.lamrani.fynoapp.R;

/**
 * Created by Lamrani on 12/12/2017.
 */

public class HomeVehicleStats {

    private Context context;

    public HomeVehicleStats(Context context){
        this.context = context;
    }

    @SerializedName("all_sm")
    private Integer total_tracking_items;

    @SerializedName("started")
    private Integer engine_on;

    @SerializedName("all_notifications")
    private Integer total_alerts;

    public Integer getTotal_tracking_items() {
        return total_tracking_items;
    }

    public Integer getEngine_on() {
        return engine_on;
    }

    public Integer getTotal_alerts() {
        return total_alerts;
    }

    /*
    public String getTotal_tracking_items() {

        if(total_tracking_items != null){
            return String.valueOf(total_tracking_items);
        }

        return String.valueOf(total_engine_on);
    }

    public String getEngine_on() {

        if(total_engine_on != null){
            return String.valueOf(total_engine_on);
        }

        return context.getString(R.string.unavailable);
    }

    public String getTotal_alerts() {

        if(total_alerts != null){
            return String.valueOf(total_alerts);
        }

        return context.getString(R.string.unavailable);
    }
     */
}
