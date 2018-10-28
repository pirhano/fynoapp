package com.othmane.lamrani.fynoapp.ui.fragments.cards;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipmentDetails;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class CardGeneralInformations extends Fragment {

    private TextView tv_licence_plate, tv_driver_name, tv_engine_state, tv_engine_state_since,  tv_address, tv_speed, tv_last_signal, tv_vehicle_type, tv_move_state, tv_move_state_since, tv_key_state, tv_key_state_since;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.card_general_informations, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // config ui
        configView();

        ImageButton btn_reduce_general_info = (ImageButton) getView().findViewById(R.id.btn_reduce_general_informations);

        refresh_general_informations();

    }

    private void configView() {
       if(getView() != null){
           // TextViews
           tv_licence_plate = getView().findViewById(R.id.tv_licence_plate);
           tv_driver_name = getView().findViewById(R.id.tv_driver_name);
           tv_engine_state =  getView().findViewById(R.id.tv_engine_state);
           tv_move_state = getView().findViewById(R.id.tv_move_state);
           tv_key_state = getView().findViewById(R.id.tv_key_state);
           tv_engine_state_since =  getView().findViewById(R.id.tv_engine_state_since);
           tv_move_state_since = getView().findViewById(R.id.tv_move_state_since);
           tv_key_state_since = getView().findViewById(R.id.tv_key_state_since);
           tv_address = getView().findViewById(R.id.tv_address);
           tv_last_signal = getView().findViewById(R.id.tv_last_signal);
           tv_speed = getView().findViewById(R.id.tv_speed);
           tv_vehicle_type = getView().findViewById(R.id.tv_vehicle_type);
       }
    }

    /**
     * Configure general informations card
     */
    public void refresh_general_informations() {

        // address
        String st_address = "--";
        /*if(FragmentEquipmentDetails.currentTrackingDynData.getGeopoint() != null){
            st_address = Methods.getAddress(getContext(), FragmentEquipmentDetails.currentTrackingDynData.getGeopoint());
        }*/
        // engine state
        String st_engine_state = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_engine_state() == 1){
            st_engine_state = getString(R.string.vehicle_engine_is_on);
        }
        else{
            st_engine_state = getString(R.string.vehicle_engine_is_off);
        }

        // engine state since
        if(FragmentEquipmentDetails.currentTrackingDynData.getKey_state_since(getContext()) != null){
            String st_engine_state_since = getString(R.string.since) + " " + FragmentEquipmentDetails.currentTrackingDynData.getKey_state_since(getContext());
            tv_engine_state_since.setVisibility(View.VISIBLE);
            tv_engine_state_since.setText(st_engine_state_since);
        }
        else{
            tv_engine_state_since.setVisibility(View.GONE);
        }

        // MOVE state
        String st_move_state = "Unavailable";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_move_state() == 1){
            st_move_state = getString(R.string.yes);
        }
        else{
            st_move_state = getString(R.string.no);
        }

        // move state since
        if(FragmentEquipmentDetails.currentTrackingDynData.getMove_state_since(getContext()) != null){
            String st_move_state_since = getString(R.string.since) + " " + FragmentEquipmentDetails.currentTrackingDynData.getMove_state_since(getContext());
            tv_move_state_since.setVisibility(View.VISIBLE);
            tv_move_state_since.setText(st_move_state_since);
        }
        else{
            tv_move_state_since.setVisibility(View.GONE);
        }

        // key state
        String st_key_state = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_key_state() == 1){
            st_key_state = getString(R.string.yes);
        }
        else{
            st_key_state = getString(R.string.no);
        }

        // key state since
        if(FragmentEquipmentDetails.currentTrackingDynData.getKey_state_since(getContext()) != null){
            String st_key_state_since = getString(R.string.since) + " " + FragmentEquipmentDetails.currentTrackingDynData.getKey_state_since(getContext());
            tv_key_state_since.setVisibility(View.VISIBLE);
            tv_key_state_since.setText(st_key_state_since);
        }
        else{
            tv_key_state_since.setVisibility(View.GONE);
        }

        // speed
        String st_speed = "--";
        if(FragmentEquipmentDetails.currentTrackingDynData.getLast_gps_speed() != null){
            if(FragmentEquipmentDetails.currentTrackingDynData.getLast_gps_speed() > 0){
                st_speed = String.valueOf(FragmentEquipmentDetails.currentTrackingDynData.getLast_gps_speed() + " Km/h");
            }
            else{
                st_speed = "--";
            }
        }


        // last signal
        String st_last_signal = (FragmentEquipmentDetails.currentTrackingDynData.getLast_timestamp_cs_DateTime(getContext()) != null) ?  FragmentEquipmentDetails.currentTrackingDynData.getLast_timestamp_cs_DateTime(getContext()) : "--";

        if(!FragmentEquipmentDetails.currentTrackingDynData.isTodaySignal()){
            tv_last_signal.setTextColor(Color.RED);
        }

        // static vars
        tv_licence_plate.setText(FragmentEquipmentDetails.st_licence_plate);
        tv_driver_name.setText(FragmentEquipmentDetails.st_driver_name);
        tv_vehicle_type.setText(FragmentEquipmentDetails.st_vehicle_type);

        // dynamic vars that change with every refresh action
        tv_engine_state.setText(st_engine_state);
        tv_move_state.setText(st_move_state);
        tv_key_state.setText(st_key_state);
        tv_address.setText(st_address);
        tv_last_signal.setText(st_last_signal);
        tv_speed.setText(st_speed);

    }



}
