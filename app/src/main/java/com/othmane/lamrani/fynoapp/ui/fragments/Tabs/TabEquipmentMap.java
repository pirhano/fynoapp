package com.othmane.lamrani.fynoapp.ui.fragments.Tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.othmane.lamrani.fynoapp.API.models.EquipementData;
import com.othmane.lamrani.fynoapp.API.models.TrackingDynData;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.helper.RequestPermission;
import com.othmane.lamrani.fynoapp.helper.osmdroid.MapHelper;
import com.othmane.lamrani.fynoapp.API.callback.MapFeaturesAPI;
import com.othmane.lamrani.fynoapp.ui.MainActivity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class TabEquipmentMap extends RequestPermission {

    public static MapView map;
    private MapFeaturesAPI service;
    private MapHelper mapHelper;
    public static ProgressBar progressBar;
    public static View dark_bg;
    private static List<Marker> trackingDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_equipment_map, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Map container
        map = (MapView) getView().findViewById(R.id.map);
        // progress bar
        progressBar = (ProgressBar) getView().findViewById(R.id.progressbar);
        dark_bg = (View) getView().findViewById(R.id.bg_dark);

        // Map helper
        mapHelper = new MapHelper(getContext(), map);

        // configure the map
        configureMap();

        trackingDataList = new ArrayList<>();

    }

    public void configureMap(){
        // get the Map API
        service = mapHelper.getConfigRetrofitCall();
        if(service != null){
            // configure the map
            mapHelper.configureMap(service);
        }

        // set the zoom and the center odf the map
        map.getController().setZoom(8);
        map.getController().setCenter(new GeoPoint(34.251632, -6.585407));

        // disable scroll parent when the map is touched
        final RelativeLayout linearLayoutContainer = (RelativeLayout) getView().findViewById(R.id.content_equipment_map);
        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                linearLayoutContainer.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public static void refreshVehiclesPosition(Context context){
        if(context != null){
            // clear all previous markers if exists
            if(!trackingDataList.isEmpty()){
                map.getOverlays().addAll(trackingDataList);
            }


            if(MainActivity.vehicleList != null){
                if(!MainActivity.vehicleList.isEmpty()){
                    for (EquipementData equipementData : MainActivity.vehicleList) {
                        final Marker vehicle_marker = new Marker(map);

                        if(equipementData.getTracking_dyn_data().getGeopoint() != null ){
                            vehicle_marker.setPosition(equipementData.getTracking_dyn_data().getGeopoint());
                            if(equipementData.getTracking_dyn_data().getLast_course() != null){
                                vehicle_marker.setRotation(equipementData.getTracking_dyn_data().getLast_course() - 90);
                            }
                        }


                        // set the icon
                        int id_icon = R.drawable.ic_sm_red_4;
                        if(equipementData.getTracking_dyn_data().getLast_engine_state() == 1){
                            id_icon = R.drawable.ic_sm_green_4;
                        }

                        Drawable icon = context.getResources().getDrawable(id_icon, null);

                        vehicle_marker.setIcon(icon);


                        final InfoWindow custInfoWindow = new InfoWindow(R.layout.custom_info_window_equipment_onclick_details, map) {
                            @Override
                            public void onOpen(Object item) {

                            }

                            @Override
                            public void onClose() {

                            }
                        };

                        vehicle_marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                map.getController().setCenter(vehicle_marker.getPosition());
                                InfoWindow.closeAllInfoWindowsOn(mapView);
                                vehicle_marker.showInfoWindow();
                                return true;
                            }
                        });

                        // configure custom info window informations
                        configureCustomInfoWindow((MainActivity) context, custInfoWindow, equipementData);

                        // affect this customized infowindow to the vehicle marker
                        vehicle_marker.setInfoWindow(custInfoWindow);

                        // add the vehicle marker to the list of the overlays
                        map.getOverlays().add(vehicle_marker);

                        // add this vehicle marker to the list of all markers in order to remove them from the map when needed
                        trackingDataList.add(vehicle_marker);

                        // submit changes to the map
                        map.invalidate();
                    }
                }
            }


        }
    }

    /**
     * Configure informations on infowindow when the vehicle icon is clicked
     */
    public static void configureCustomInfoWindow(final MainActivity context, InfoWindow custInfoWindow, final EquipementData equipment) {

        TextView tv_info_details, tv_info_licence_plate, tv_info_driver_name, tv_info_engine_state, tv_info_vehicle_type, tv_info_speed, tv_info_last_signal, tv_info_address;
        ImageView iv_info_key_state;

        // TextViews
        tv_info_licence_plate = custInfoWindow.getView().findViewById(R.id.tv_licence_plate);
        tv_info_vehicle_type = custInfoWindow.getView().findViewById(R.id.tv_vehicle_type);
        tv_info_driver_name = custInfoWindow.getView().findViewById(R.id.tv_driver_name);
        tv_info_engine_state =  custInfoWindow.getView().findViewById(R.id.tv_engine_state);
        iv_info_key_state = custInfoWindow.getView().findViewById(R.id.iv_key_state);
        tv_info_speed = custInfoWindow.getView().findViewById(R.id.tv_speed);
        tv_info_last_signal = custInfoWindow.getView().findViewById(R.id.tv_last_signal);
        tv_info_address = custInfoWindow.getView().findViewById(R.id.tv_address);
        tv_info_details = custInfoWindow.getView().findViewById(R.id.tv_details);
        tv_info_details.setText(R.string.tracking);


        if(tv_info_details != null){
            tv_info_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InfoWindow.closeAllInfoWindowsOn(map);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.REFERENCE.EQUIPMENT, equipment.getTracking_dyn_data());

                    // show Equipment details fragment
                    context.displaySelectedScreen(R.id.nav_equipment_details, b);
                }
            });
        }

        TrackingDynData item = equipment.getTracking_dyn_data();

        String st_licence_plate = (item.getLicence_plate() != null) ?  item.getLicence_plate() : "--";
        String st_driver_name = "--";

        String st_address = "--";

        // speed
        String st_speed = "--";
        if(item.getLast_gps_speed() != null){
            if(item.getLast_gps_speed() > 0){
                st_speed = String.valueOf(item.getLast_gps_speed() + " Km/h");
            }
            else{
                st_speed = "--";
            }
        }
        String st_engine_state = "--";

        if(item.getLast_engine_state() == 1){
            st_engine_state = context.getString(R.string.vehicle_engine_is_on);
        }
        else{
            st_engine_state = context.getString(R.string.vehicle_engine_is_off);
        }

        String st_key_state = "--";
        if(item.getLast_key_state() == 1){
            iv_info_key_state.setImageResource(R.drawable.ic_key_on);
        }
        else{
            iv_info_key_state.setImageResource(R.drawable.ic_key);
        }


        String st_vehicle_type = "--";
        if(item.getLabel() != null){
            st_vehicle_type = item.getLabel();
        }



        String st_last_signal = (item.getLast_timestamp_cs_DateTime(context) != null) ?  item.getLast_timestamp_cs_DateTime(context) : "Unavailable";

        tv_info_licence_plate.setText(st_licence_plate);
        tv_info_vehicle_type.setText(st_vehicle_type);
        tv_info_driver_name.setText(st_driver_name);
        tv_info_engine_state.setText(st_engine_state);
        tv_info_speed.setText(st_speed);
        tv_info_last_signal.setText(st_last_signal);
        tv_info_address.setText(st_address);

    }


    @Override
    public void onPermissionsGranted(int requestCode) {

    }
}





