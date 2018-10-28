package com.othmane.lamrani.fynoapp.ui.fragments.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipmentDetails;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class CardDeatilsInformations extends Fragment {

    private TextView tv_counter, tv_timer, tv_counter_today, tv_timer_today, tv_fuel_level, tv_max_speed, tv_first_key_on, tv_first_engine_on, tv_first_activity;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.card_vehicle_general_detailed, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // config ui
        configView();

        ImageButton btn_reduce_details_informations = (ImageButton) getView().findViewById(R.id.btn_reduce_consumption);

        refresh_more_informations_card();

    }

    private void configView() {
       if(getView() != null){
           tv_counter = getView().findViewById(R.id.tv_counter);
           tv_timer = getView().findViewById(R.id.tv_timer);
           tv_counter_today = getView().findViewById(R.id.tv_counter_today);
           tv_timer_today = getView().findViewById(R.id.tv_timer_today);
           tv_fuel_level = getView().findViewById(R.id.tv_fuel_level);
           tv_max_speed = getView().findViewById(R.id.tv_max_speed);
           tv_first_key_on = getView().findViewById(R.id.tv_first_key_on);
           tv_first_engine_on = getView().findViewById(R.id.tv_first_engine_on);
           tv_first_activity = getView().findViewById(R.id.tv_first_activity);

       }
    }

    /**
     * Refresh more information
     *
     */
    public void refresh_more_informations_card(){
        String st_counter = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_distance_counter() != null){
            st_counter = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getLast_distance_counter()) + " Km";
        }

        String st_counter_today = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_today_distance_counter() != null){
            st_counter_today = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getLast_today_distance_counter()) + " Km";
        }

        String st_timer = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_time_counter() != null){
            st_timer = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getLast_time_counter()) + " H";
        }

        String st_timer_today = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_today_time_counter() != null){
            st_timer_today = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getLast_today_time_counter()) + " H";
        }



        String st_fuel_level = "--";

        String st_max_speed = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getToday_max_speed() != null){
            if(FragmentEquipmentDetails.currentTrackingDynData.getToday_max_speed() > 0){
                st_max_speed = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getToday_max_speed()) + " Km/h";
            }
            else {
                st_max_speed = "--";
            }
        }

        String st_first_key_on = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getToday_first_key_on(getContext()) != null){
            st_first_key_on = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getToday_first_key_on(getContext()));
        }

        String st_first_engine_on = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getToday_first_engine_on(getContext()) != null){
            st_first_engine_on = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getToday_first_engine_on(getContext()));
        }

        String st_first_activity = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getToday_first_activity_DateTime(getContext()) != null){
            st_first_activity = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getToday_first_activity_DateTime(getContext()));
        }



        tv_counter.setText(st_counter);
        tv_counter_today.setText(st_counter_today);
        tv_timer.setText(st_timer);
        tv_timer_today.setText(st_timer_today);
        tv_fuel_level.setText(st_fuel_level);
        tv_max_speed.setText(st_max_speed);
        tv_first_key_on.setText(st_first_key_on);
        tv_first_engine_on.setText(st_first_engine_on);
        tv_first_activity.setText(st_first_activity);
    }



}
