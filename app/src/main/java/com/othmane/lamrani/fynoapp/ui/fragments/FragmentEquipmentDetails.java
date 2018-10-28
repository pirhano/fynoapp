package com.othmane.lamrani.fynoapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.Stop;
import com.othmane.lamrani.fynoapp.API.models.StopData;
import com.othmane.lamrani.fynoapp.API.models.TodayPath;
import com.othmane.lamrani.fynoapp.API.models.TripStops;
import com.othmane.lamrani.fynoapp.API.models.Waypoint;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.controller.StopsController;
import com.othmane.lamrani.fynoapp.controller.TrackingItemsController;
import com.othmane.lamrani.fynoapp.controller.WayPointsController;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.helper.RequestPermission;
import com.othmane.lamrani.fynoapp.helper.osmdroid.MapHelper;
import com.othmane.lamrani.fynoapp.API.callback.MapFeaturesAPI;
import com.othmane.lamrani.fynoapp.API.models.TrackingDynData;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.cards.CardDeatilsInformations;
import com.othmane.lamrani.fynoapp.ui.fragments.cards.CardGeneralInformations;
import com.othmane.lamrani.fynoapp.ui.fragments.cards.CardSpeedChart;
import com.othmane.lamrani.fynoapp.ui.fragments.cards.CardStops;
import com.othmane.lamrani.fynoapp.ui.fragments.cards.CardTrips;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.othmane.lamrani.fynoapp.helper.osmdroid.MapHelper.MAX_POINTS;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class FragmentEquipmentDetails extends RequestPermission implements MainActivity.OnBackPressedListener{

    public static MapView map;
    public static ImageButton btn_map_menu;
    private LinearLayout btn_map_vehicle_position;
    private Switch switch_real_time;
    public static RelativeLayout container_black_background;
    public static Fragment current_fragment;
    private LinearLayout  container_map_menu, container_actions ;
    private LinearLayout btn_general, btn_details, btn_actions, btn_trips, btn_history, btn_speed;
    private LinearLayout container_info_speed;
    private TextView tv_info_map, tv_info_speed, tv_info_speed_km_h;
    private MapFeaturesAPI mapService;
    private Marker vehicleMarker;
    private Switch switch_load_todayPath, switch_load_today_stops, switch_load_user_zones;
    private MapHelper mapHelper;
    public static TrackingDynData currentTrackingDynData;
    //public static EquipementData currentTrackingDynData;
    private ImageButton btn_refresh;
    private ProgressBar progressBar;
    private View dark_bg;
    private InfoWindow custInfoWindow_vehicle_details;
    private TrackingItemsController trackingItemsController;
    public WayPointsController wayPointsController;
    public StopsController stopsController;
    public Polyline pathOverlay;
    public static String st_licence_plate, st_driver_name, st_vehicle_type;
    private Call<TrackingDynData> equipmentCall;
    private Call<TodayPath> way_points_call;
    private Call<Stop> stops_call;
    private Handler handler_realtime;
    private boolean real_time;
    private boolean menu_opened;
    private static boolean info_card_open;
    private boolean menu_child_expanded;
    public static FragmentTransaction fragmentTransaction;
    private static FrameLayout frameLayout;
    public static Marker stop_marker;
    private Thread updateThread = null;

    public List<List<Marker>> stop_markers;
    public List<List<Marker>> direction_markers;
    public List<Polyline> polylines;

    //private List<Waypoint> waypoints;
    private MapListener smoothPathScrollListener;

    private CardTrips cardTrips;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_equipment_details, container, false);

        View rootView = inflater.inflate(R.layout.fragment_equipment_details, container, false);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        current_fragment = null;

        // if the call is alrealy executing we are going to cancel it
        if(equipmentCall != null){
            if(equipmentCall.isExecuted()){
                equipmentCall.cancel();
            }
        }
        // if the call is alrealy executing we are going to cancel it
        if(stops_call != null){
            if(stops_call.isExecuted()){
                stops_call.cancel();
            }
        }
        // if the call is alrealy executing we are going to cancel it
        if(way_points_call != null){
            if(way_points_call.isExecuted()){
                way_points_call.cancel();
            }
        }
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure the views
        configView();

        // getting the equipment passed
        Bundle bundle = this.getArguments();
        if(bundle != null){
            currentTrackingDynData = (TrackingDynData) bundle.getSerializable(Constants.REFERENCE.EQUIPMENT);
        }

        // onback pressed listener
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        if(currentTrackingDynData != null){

            // Set the title of the fragment activity
            getActivity().setTitle(currentTrackingDynData.getLicence_plate());

            // boolean that tell us wether if we are using real time tracking or not
            real_time = false;
            // boolean that tell us wether if the map menu is expanded or not
            menu_opened = false;
            // boolean that tell us wether if the child  menu is expanded or not
            menu_child_expanded = false;
            // boolean that tell us wether if the card menu is opened or not
            info_card_open = false;


            // initializing
            trackingItemsController = new TrackingItemsController();
            wayPointsController = new WayPointsController();
            stopsController = new StopsController();

            stop_markers = new ArrayList<>();
            direction_markers = new ArrayList<>();
            polylines = new ArrayList<>();

            prepareLists();

            // Configure btn for inflating menu
            btn_map_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "Clicked option menu", Toast.LENGTH_SHORT).show();
                    if(menu_opened){
                        hide_map_menu();
                    }
                    else{
                        InfoWindow.closeAllInfoWindowsOn(map);
                        show_map_menu();
                    }
                }
            });


            // configure btn for general informations
            btn_general.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), "General informations", Toast.LENGTH_SHORT).show();
                    showCard(new CardGeneralInformations());
                }
            });

            // configure btn for details
            btn_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), "Details", Toast.LENGTH_SHORT).show();
                    showCard(new CardDeatilsInformations());
                }
            });

            // configure btn for map actions (drawing ....)
            btn_actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(menu_child_expanded){
                        //container_actions.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_down));
                        container_actions.setVisibility(View.GONE);

                        menu_child_expanded = false;
                    }
                    else{
                        container_actions.setVisibility(View.VISIBLE);
                        //container_actions.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));

                        menu_child_expanded = true;
                    }
                }
            });

            // configure btn for routing
            btn_trips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), "Liste des stops", Toast.LENGTH_SHORT).show();
                    hide_map_menu();
                    //load_vehicle_stops(null);
                    if(cardTrips == null){
                        cardTrips = new CardTrips();
                    }
                    showCard(cardTrips);
                }
            });

            // configure btn for vehicle history
            btn_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* hide_map_menu();

                    //Toast.makeText(getContext(), "Historique du véhicule", Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();
                    b.putBoolean(Constants.REFERENCE.EQUIPMENT_back_to_details, true);
                    b.putString(Constants.REFERENCE.EQUIPMENT_licence_plate, currentTrackingDynData.getLicence_plate());
                    b.putSerializable(Constants.REFERENCE.EQUIPMENT, currentTrackingDynData);

                    ((MainActivity)getActivity()).displaySelectedScreen(R.id.nav_history, b);*/
                }
            });

            // configure btn for speed
            btn_speed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hide_map_menu();

                    //load_vehicle_stops(null);
                    showCard(new CardSpeedChart());
                }
            });



            // configuring checkboxes
            switch_load_todayPath.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    hide_map_menu();
                    if(isChecked){
                        drawTodayPath();
                    }
                    else {
                        removeTodayPath();
                    }
                }
            });

            // load today stops
            switch_load_today_stops.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    hide_map_menu();
                    if(isChecked){
                        drawTodayStops();
                    }
                    else{
                        removeTodayStops();
                    }
                }
            });

            // realtime
            switch_real_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        real_time = true;
                        refreshVehicleInformations(true);
                    }
                    else{
                        disable_real_time();
                    }
                }
            });


            // initialize variables that doesn't change in realTime
            st_licence_plate = (currentTrackingDynData.getLicence_plate() != null) ?  currentTrackingDynData.getLicence_plate() : "--";
            st_driver_name = "--";
            st_vehicle_type = (currentTrackingDynData.getLabel() != null) ?  currentTrackingDynData.getLabel() : "--";

            // Configure the position of the vehicle on the map
            refreshVehiclePosition();

            // refresh know from the server side
            refreshVehicleInformations(true);


            // refresh button
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Methods.isNetworkAvailable(getContext())){

                        refreshVehicleInformations(true);
                    }
                    else{
                        //Toast.makeText(getContext(), getString(R.string.network_disabled), Toast.LENGTH_SHORT).show();
                        Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
                    }
                }
            });



            // Button to get the position of the vehicle on the map
            btn_map_vehicle_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hide_map_menu();
                    if(currentTrackingDynData.getGeopoint() != null){
                        map.getController().animateTo(currentTrackingDynData.getGeopoint());
                        map.getController().setCenter(currentTrackingDynData.getGeopoint());
                        refreshVehicleInformations(true);
                    }
                }
            });

        }
        else{
            //Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_LONG).show();
            Snackbar.make(view, getString(R.string.intern_error), Snackbar.LENGTH_LONG).show();
        }



    }

    private void prepareLists() {
        int i=0;
        Polyline polyline = new Polyline();
        List<Marker> directions = new ArrayList<Marker>();
        Marker stop_marker = new Marker(map);
        while (i<10){
            polylines.add(polyline);
            direction_markers.add(directions);
            stop_markers.add(stop_marker);
            i++;
        }
        //polylines.add(null);
    }


    /**
     * Refresh all the informations of the clicked vehicle
     * it calls the tracking dyn data and update all fields except driver name, licence plate and vehicle type
     */
    public void refreshVehicleInformations(boolean show_loading_progress){
        //Toast.makeText(getContext(), "Loading ...", Toast.LENGTH_SHORT).show();

        if(Methods.isNetworkAvailable(getContext())){
            if(getView() != null){
                // snackbar showing loading data
                final Snackbar snackbar = Snackbar.make(getView(), getString(R.string.loading), Snackbar.LENGTH_INDEFINITE);

                if(currentTrackingDynData.getTracking_item_id() != null){

                    if(show_loading_progress){
                        snackbar.show();
                        showProgressBar();
                    }


                    equipmentCall = trackingItemsController.getLastDynData(currentTrackingDynData.getTracking_item_id());


                    equipmentCall.enqueue(new Callback<TrackingDynData>() {
                        @Override
                        public void onResponse(Call<TrackingDynData> call, Response<TrackingDynData> response) {

                            Log.i("Retrofit", "success");

                            if(response.body() != null){
                                currentTrackingDynData = response.body();
                                if(currentTrackingDynData != null){
                                    // Configure the position of the vehicle on the map and other stuffs
                                    //refresh vehicle speed view
                                    if(currentTrackingDynData.getLast_gps_speed() != null){

                                        container_info_speed.setVisibility(View.VISIBLE);

                                        if(currentTrackingDynData.getLast_gps_speed() > 0){
                                            // show vehicle speed
                                            tv_info_speed_km_h.setVisibility(View.VISIBLE);
                                            tv_info_speed.setText(String.valueOf(currentTrackingDynData.getLast_gps_speed()));
                                        }
                                        else{
                                            tv_info_speed.setText(getString(R.string.still_stopped));
                                            tv_info_speed_km_h.setVisibility(View.GONE);
                                        }
                                    }
                                    else{
                                        container_info_speed.setVisibility(View.GONE);
                                    }

                                    if(!real_time){
                                        // timer
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable(){
                                            @Override
                                            public void run(){
                                                container_info_speed.setVisibility(View.GONE);
                                            }
                                        }, 5000);
                                    }

                                    refreshVehiclePosition();
                                    if(current_fragment != null){
                                        if(current_fragment instanceof CardGeneralInformations){
                                            ((CardGeneralInformations)current_fragment).refresh_general_informations();
                                        }
                                        else if(current_fragment instanceof CardDeatilsInformations){
                                            ((CardDeatilsInformations)current_fragment).refresh_more_informations_card();
                                        }
                                    }

                                }
                            }
                            else{
                                //Toast.makeText(getActivity(), getString(R.string.intern_error), Toast.LENGTH_LONG).show();
                                Snackbar.make(getView(), getString(R.string.intern_error), Snackbar.LENGTH_LONG).show();
                            }

                            snackbar.dismiss();
                            hideProgressBar();


                            if(real_time){
                                // timer 15s
                                handler_realtime = new Handler();
                                handler_realtime.postDelayed(new Runnable(){
                                    @Override
                                    public void run(){
                                        refreshVehicleInformations(false);
                                    }
                                }, 20000);
                            }

                        }

                        @Override
                        public void onFailure(Call<TrackingDynData> call, Throwable t) {

                            disable_real_time();

                            if(!call.isCanceled()){
                                Toast.makeText(getActivity(), getString(R.string.intern_error) + " \n" + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            else{
                                //Toast.makeText(getContext(), "Loading data cancelled", Toast.LENGTH_SHORT).show();
                                Log.i("Retrofit", "Cancelled");
                            }

                            snackbar.dismiss();
                            hideProgressBar();
                        }
                    });


                }
                else{
                    disable_real_time();
                    //Toast.makeText(getActivity(), getString(R.string.intern_error), Toast.LENGTH_LONG).show();
                    Snackbar.make(getView(), getString(R.string.intern_error), Snackbar.LENGTH_LONG).show();
                }
            }
        }
        else{
            disable_real_time();
            hideProgressBar();
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
        }

    }

    /**
     * Disable real time
     */
    public void disable_real_time(){
        switch_real_time.setChecked(false);
        real_time = false;
    }


    /**
     * Configure the vehicle position
     */
    private void refreshVehiclePosition() {

        GeoPoint point = currentTrackingDynData.getGeopoint();

        if(point != null){

            // set a custom infowindow to show car details
            if(custInfoWindow_vehicle_details == null){
                custInfoWindow_vehicle_details = new InfoWindow(R.layout.custom_info_window_equipment_onclick_details, map) {
                    @Override
                    public void onOpen(Object item) {
                        InfoWindow.closeAllInfoWindowsOn(map);
                    }

                    @Override
                    public void onClose() {
                        InfoWindow.closeAllInfoWindowsOn(map);
                    }
                };

            }

            if(vehicleMarker == null){
                vehicleMarker = new Marker(map);
                vehicleMarker.setInfoWindow(custInfoWindow_vehicle_details);
            }

            // set the center of the map to that geopoint
            map.getController().setCenter(point);

            configureCustomInfoWindow_vehicle_details(custInfoWindow_vehicle_details);


            // get the coordinates if the vehicle and plot the marker
            vehicleMarker.setPosition(point);

            // set rotation
            if(currentTrackingDynData.getLast_course() != null){
                vehicleMarker.setRotation(currentTrackingDynData.getLast_course() - 90);
            }


            // set the icon
            int id_icon = R.drawable.ic_sm_red_4;
            if(currentTrackingDynData.getLast_engine_state() == 1){
                id_icon = R.drawable.ic_sm_green_4;
            }

            Drawable icon = getActivity().getResources().getDrawable(id_icon, null);

            vehicleMarker.setIcon(icon);

            vehicleMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    map.getController().animateTo(vehicleMarker.getPosition());
                    InfoWindow.closeAllInfoWindowsOn(mapView);
                    vehicleMarker.showInfoWindow();
                    return true;
                }
            });


            // add the matker to the map
            map.getOverlays().add(vehicleMarker);
            //map.getController().setCenter(point);

            //vehicleMarker.showInfoWindow();
            map.invalidate();

            if(vehicleMarker != null && custInfoWindow_vehicle_details != null){
                if(custInfoWindow_vehicle_details.isOpen()){
                    custInfoWindow_vehicle_details.close();
                    vehicleMarker.showInfoWindow();
                }
            }

        }

    }

    /**
     * Configure informations on infowindow when the vehicle icon is clicked
     */
    private void configureCustomInfoWindow_vehicle_details(InfoWindow custInfoWindow) {
        if(getView() != null){
            // TextViews
            TextView tv_info_details, tv_info_licence_plate, tv_info_driver_name, tv_info_engine_state, tv_info_vehicle_type, tv_info_speed, tv_info_last_signal, tv_info_address;
            ImageView iv_info_key_state;

            tv_info_licence_plate = custInfoWindow.getView().findViewById(R.id.tv_licence_plate);
            tv_info_vehicle_type = custInfoWindow.getView().findViewById(R.id.tv_vehicle_type);
            tv_info_driver_name = custInfoWindow.getView().findViewById(R.id.tv_driver_name);
            tv_info_engine_state =  custInfoWindow.getView().findViewById(R.id.tv_engine_state);
            iv_info_key_state = custInfoWindow.getView().findViewById(R.id.iv_key_state);
            tv_info_speed = custInfoWindow.getView().findViewById(R.id.tv_speed);
            tv_info_last_signal = custInfoWindow.getView().findViewById(R.id.tv_last_signal);
            tv_info_address = custInfoWindow.getView().findViewById(R.id.tv_address);
            tv_info_details = custInfoWindow.getView().findViewById(R.id.tv_details);
            tv_info_details.setText(R.string.more);

            if(tv_info_details != null){
                tv_info_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCard(new CardGeneralInformations());
                        InfoWindow.closeAllInfoWindowsOn(map);
                    }
                });
            }


            String st_address = "--";

            // speed
            String st_speed = "--";
            if(currentTrackingDynData.getLast_gps_speed() != null){
                if(currentTrackingDynData.getLast_gps_speed() > 0){
                    st_speed = String.valueOf(currentTrackingDynData.getLast_gps_speed() + " Km/h");
                }
                else{
                    st_speed = "--";
                }
            }


            if(currentTrackingDynData.getLabel() != null){
                st_vehicle_type = currentTrackingDynData.getLabel();
            }

            String st_engine_state = "--";
            if(currentTrackingDynData.getLast_engine_state() == 1){
                st_engine_state = getString(R.string.vehicle_engine_is_on);
            }
            else{
                st_engine_state = getString(R.string.vehicle_engine_is_off);
            }

            String st_key_state = "--";
            if(currentTrackingDynData.getLast_key_state() == 1){
                iv_info_key_state.setImageResource(R.drawable.ic_key_on);
            }
            else{
                iv_info_key_state.setImageResource(R.drawable.ic_key);
            }

            String st_last_signal = (currentTrackingDynData.getLast_timestamp_cs_DateTime(getContext()) != null) ?  currentTrackingDynData.getLast_timestamp_cs_DateTime(getContext()) : "--";

            if(!currentTrackingDynData.isTodaySignal()){
                tv_info_last_signal.setTextColor(Color.RED);
            }

            tv_info_licence_plate.setText(st_licence_plate);
            tv_info_vehicle_type.setText(st_vehicle_type);
            tv_info_driver_name.setText(st_driver_name);
            tv_info_engine_state.setText(st_engine_state);
            tv_info_speed.setText(st_speed);
            tv_info_last_signal.setText(st_last_signal);
            tv_info_address.setText(st_address);
        }

    }

    /**
     * Configure informations on infowindow when the vehicle icon is clicked
     */
    /*public void configureCustomInfoWindow_vehicle_stops(StopData stopData, InfoWindow custInfoWindow) {
        if(getView() != null){
            // TextViews
            TextView tv_stop_start, tv_stop_end, tv_stop_duration, tv_stop_counter_distance, tv_stop_counter_time, tv_stop_address;

            tv_stop_start = custInfoWindow.getView().findViewById(R.id.tv_stop_start);
            tv_stop_end = custInfoWindow.getView().findViewById(R.id.tv_stop_end);
            tv_stop_duration = custInfoWindow.getView().findViewById(R.id.tv_stop_duration);
            tv_stop_counter_distance =  custInfoWindow.getView().findViewById(R.id.tv_stop_counter_distance);
            tv_stop_counter_time = custInfoWindow.getView().findViewById(R.id.tv_stop_counter_time);
            tv_stop_address = custInfoWindow.getView().findViewById(R.id.tv_stop_address);


            String st_address = "--";

            // stopData start
            String st_stop_start = (stopData.getStop_timestamp_srv_DateTime(getContext()) != null) ? stopData.getStop_timestamp_srv_DateTime(getContext())  : "--";
            String st_stop_end = (stopData.getNo_stop_timestamp_srv_DateTime(getContext()) != null) ? stopData.getNo_stop_timestamp_srv_DateTime(getContext())  : "--";
            String st_stop_duration = (stopData.getStop_duration(getContext()) != null) ? stopData.getStop_duration(getContext())  : "--";
            String st_stop_counter_distance = (stopData.getDistance_counter() != null && stopData.getToday_distance_counter()!= null) ? stopData.getToday_distance_counter() + " / " + stopData.getDistance_counter() + " Km"  : "--";
            String st_stop_counter_time = (stopData.getTime_counter() != null && stopData.getToday_time_counter() != null) ? stopData.getToday_time_counter() + " / " + stopData.getTime_counter()  : "--";


            tv_stop_start.setText(st_stop_start);
            tv_stop_end.setText(st_stop_end);
            tv_stop_duration.setText(st_stop_duration);
            tv_stop_counter_distance.setText(st_stop_counter_distance);
            tv_stop_counter_time.setText(st_stop_counter_time);

        }

    }*/

    public void configureCustomInfoWindow_vehicle_stops(TripStops tripStop, InfoWindow custInfoWindow) {
        if(getView() != null){
            // TextViews
            TextView tv_stop_start, tv_stop_end, tv_stop_duration, tv_stop_counter_distance, tv_stop_counter_time, tv_stop_address;

            tv_stop_start = custInfoWindow.getView().findViewById(R.id.tv_stop_start);
            //tv_stop_end = custInfoWindow.getView().findViewById(R.id.tv_stop_end);
            tv_stop_duration = custInfoWindow.getView().findViewById(R.id.tv_stop_duration);
            //tv_stop_counter_distance =  custInfoWindow.getView().findViewById(R.id.tv_stop_counter_distance);
            //tv_stop_counter_time = custInfoWindow.getView().findViewById(R.id.tv_stop_counter_time);
            tv_stop_address = custInfoWindow.getView().findViewById(R.id.tv_stop_address);


            String st_address = "--";

            // stopData start
            String st_stop_start = (tripStop.getStop_timestamp(getContext()) != null) ? tripStop.getStop_timestamp(getContext())  : "--";
            //String st_stop_end = (stopData.getNo_stop_timestamp_srv_DateTime(getContext()) != null) ? stopData.getNo_stop_timestamp_srv_DateTime(getContext())  : "--";
            String st_stop_duration = (tripStop.getStop_duration() != null) ? tripStop.getStop_duration()  : "--";
            //String st_stop_counter_distance = (stopData.getDistance_counter() != null && stopData.getToday_distance_counter()!= null) ? stopData.getToday_distance_counter() + " / " + stopData.getDistance_counter() + " Km"  : "--";
            //String st_stop_counter_time = (stopData.getTime_counter() != null && stopData.getToday_time_counter() != null) ? stopData.getToday_time_counter() + " / " + stopData.getTime_counter()  : "--";


            tv_stop_start.setText(st_stop_start);
            //tv_stop_end.setText(st_stop_end);
            tv_stop_duration.setText(st_stop_duration);
            //tv_stop_counter_distance.setText(st_stop_counter_distance);
            //tv_stop_counter_time.setText(st_stop_counter_time);

        }

    }


    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        dark_bg.setVisibility(View.VISIBLE);
        btn_map_menu.setEnabled(false);
    }

    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        dark_bg.setVisibility(View.GONE);
        btn_map_menu.setEnabled(true);
    }

    /**
     * Configure view
     */
    private void configView() {

        if(getView() != null){

            map = (MapView) getView().findViewById(R.id.map);
            // Map helper
            mapHelper = new MapHelper(getContext(), map);

            frameLayout = getView().findViewById(R.id.cards);

            // configure the map
            configureMap();

            btn_refresh = (ImageButton) MainActivity.toolbar.findViewById(R.id.btn_refresh);
            progressBar = (ProgressBar) getView().findViewById(R.id.progressbar);
            dark_bg = (View) getView().findViewById(R.id.bg_dark);


            container_black_background = (RelativeLayout) getView().findViewById(R.id.container_feature);

            container_map_menu = (LinearLayout) getView().findViewById(R.id.container_map_menu);
            container_actions = (LinearLayout) getView().findViewById(R.id.container_map_actions);

            btn_map_menu = (ImageButton) getView().findViewById(R.id.btn_map_menu);

            switch_real_time = (Switch) getView().findViewById(R.id.switch_real_time);
            switch_load_todayPath = (Switch) getView().findViewById(R.id.switch_load_today_path);
            switch_load_today_stops = (Switch) getView().findViewById(R.id.switch_load_today_stops);
            switch_load_user_zones = (Switch) getView().findViewById(R.id.switch_load_user_zones);


            btn_map_vehicle_position = (LinearLayout) getView().findViewById(R.id.btn_vehicle_position);
            btn_general = (LinearLayout) getView().findViewById(R.id.btn_general);
            btn_details = (LinearLayout) getView().findViewById(R.id.btn_details);
            btn_actions = (LinearLayout) getView().findViewById(R.id.btn_actions);
            btn_trips = (LinearLayout) getView().findViewById(R.id.btn_trips);
            btn_history = (LinearLayout) getView().findViewById(R.id.btn_history);
            btn_speed = (LinearLayout) getView().findViewById(R.id.btn_speed);


            tv_info_map = (TextView) getView().findViewById(R.id.tv_info_map);
            container_info_speed = (LinearLayout) getView().findViewById(R.id.container_info_speed);
            tv_info_speed_km_h = (TextView) getView().findViewById(R.id.tv_info_speed_km_h);
            tv_info_speed = (TextView) getView().findViewById(R.id.tv_info_speed);


        }

    }


    // show menu for map settings
    public void show_map_menu(){
        if(container_map_menu != null){
            container_map_menu.setVisibility(View.VISIBLE);
            container_black_background.setVisibility(View.VISIBLE);
            // Slide the container from the bottom of the screen
            container_map_menu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_horizentally));

            menu_opened = true;
            container_black_background.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hide_map_menu();
                    return true;
                }
            });

        }
    }

    // hide menu for map settings
    public void hide_map_menu(){
        if(container_map_menu != null){
            container_map_menu.setVisibility(View.GONE);
            container_black_background.setVisibility(View.GONE);
            // Slide the container from the bottom of the screen
            container_map_menu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_horizentally));

            menu_opened = false;

        }
    }


    /**
     * Show the current selected
     * @param fragment
     */
    private void showCard(Fragment fragment) {
        // hide the menu if its opened

        // change the fragment
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        fragmentTransaction.show(fragment);

        // Replace the fragment
        fragmentTransaction.replace(R.id.cards, fragment);

        fragmentTransaction.commit();

        if(menu_opened){
            hide_map_menu();
        }

        info_card_open = true;
         current_fragment = fragment;

        // disable clicks on buttons
        disable_click_on_map_controls();

        frameLayout.setVisibility(View.VISIBLE);
        // Slide the container from the bottom of the screen
        frameLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_1s));

        // disable clicks on the black view
        container_black_background.setVisibility(View.VISIBLE);

        if(!(fragment instanceof CardSpeedChart)){

            container_black_background.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideCard(current_fragment);
                    return true;
                }
            });
        }
    }


    // hide card info
    public static void hideCard(Fragment fragment){
        if(fragment != null){
            frameLayout.setVisibility(View.GONE);

            fragmentTransaction.hide(fragment);

            //fragmentTransaction.commit();


            //fragmentTransaction.detach(fragment);
            info_card_open = false;

            container_black_background.setVisibility(View.GONE);

            // allow clicks on map controls
            allow_click_on_map_controls();
        }
        else{
            Log.i("Fragment hide", "not found");
        }
    }

    // allow clicks on map controls
    public static void allow_click_on_map_controls(){
        btn_map_menu.setEnabled(true);
    }

    // disable clicks on map controls
    public void disable_click_on_map_controls(){
        btn_map_menu.setEnabled(false);
    }


    /**
     * Onback pressed listener
     */
    @Override
    public void doBack() {

        if(info_card_open){
            hideCard(current_fragment);
        }
        else if (menu_opened){
            hide_map_menu();
        }
        else if(!InfoWindow.getOpenedInfoWindowsOn(map).isEmpty()){
            InfoWindow.closeAllInfoWindowsOn(map);
        }
        else{
            ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_equipments, null);
        }

    }


    public void drawTrack(int position, List<GeoPoint> points, List<Double> courses, List<TripStops> tripStops){

        final ProgressDialog progressDialog;

        List<Marker> direction_arrows = new ArrayList<>();

        List<GeoPoint> track = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.menu_today_route));
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setIcon(R.drawable.ic_address);
        progressDialog.setCancelable(false);


        if(Methods.isNetworkAvailable(getContext())){

            progressDialog.show();

            // add geopoints
            for (int i=0; i<points.size(); i++) {
                if(points.get(i) != null){
                    if(points.get(i) != null){
                        // add this point
                        track.add(points.get(i));
                    }
                }
            }

            pathOverlay = new Polyline();

            if(points.isEmpty()) {//|| todayPath.getLast_today_distance_counter() == 0){
                Toast.makeText(getContext(), getString(R.string.today_path_not_found), Toast.LENGTH_LONG).show();
                switch_load_todayPath.setChecked(false);
            }
            else{
                // get all points to add a pathOverlay to the map
                pathOverlay.setPoints(track);

                polylines.add(position, pathOverlay);

                // refresh track overlay
                if(map.getOverlays().contains(polylines)){
                    map.getOverlays().removeAll(polylines);
                }

                map.getOverlays().addAll(polylines);

                //refreshTrack();
                // add start marker

               /*
               today_path_start_marker = new Marker(map);
               today_path_start_marker.setPosition(points.get(0));
               today_path_start_marker.setTitle(getString(R.string.first_activity) + ": " + currentTrackingDynData.getToday_first_activity_Time(getContext()));                map.getOverlays().add(position+2, today_path_start_marker);*/

                // add path directions
                for(int i=0; i< points.size(); i++){

                    if(i%5 == 0){
                        Marker marker = new Marker(map);
                        marker.setPosition(points.get(i));
                        marker.setRotation((courses.get(i)).floatValue());
                        marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_direction_arrow));
                        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                return true;
                            }
                        });
                        direction_arrows.add(marker);
                    }
                }

                direction_markers.add(position, direction_arrows);

                if(map.getOverlays().contains(direction_markers)){
                    map.getOverlays().removeAll(direction_markers);
                }

                map.getOverlays().addAll(direction_arrows);

                // add stops
                drawTripStops(position, tripStops);

               /* // add the route to the map smoothly with this function
                drawRoute(getContext(), map);

                map.invalidate();

                smoothPathScrollListener = new MapListener() {
                    @Override
                    public boolean onScroll(ScrollEvent event) {
                        drawRoute(getActivity(), map);
                        return false;
                    }

                    @Override
                    public boolean onZoom(ZoomEvent event) {
                        drawRoute(getActivity(), map);
                        //Toast.makeText(getContext(), "Zoom: " + event.getZoomLevel() , Toast.LENGTH_SHORT).show();
                        updateDirectionMarker(event.getZoomLevel());
                        return false;
                    }
                };

                // map listener
                map.setMapListener(smoothPathScrollListener);

                */

                map.invalidate();

            }

            progressDialog.dismiss();

        }
        else{
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
            switch_load_today_stops.setChecked(false);
        }

    }

    private void drawTripStops(int position, List<TripStops> tripStops){
        for (TripStops stop : tripStops){
            Marker marker = new Marker(map);
            marker.setPosition(stop.getGeoPoint());
            marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop_marker));
            InfoWindow custInfoWindow_stops = new InfoWindow(R.layout.custom_info_window_onclick_stops, map) {
                @Override
                public void onOpen(Object item) {
                    InfoWindow.closeAllInfoWindowsOn(map);
                }

                @Override
                public void onClose() {
                    InfoWindow.closeAllInfoWindowsOn(map);
                }
            };

            configureCustomInfoWindow_vehicle_stops(stop, custInfoWindow_stops);

            marker.setInfoWindow(custInfoWindow_stops);

            map.getOverlays().add(marker);

            // add those markers to the map
            stop_markers.add(position, marker);
        }
    }

    private void refreshTrack() {

        /*// refreshing polylines
        map.getOverlays().removeAll(polylines);
        map.getOverlays().addAll(polylines);

        // refreshing direction markers
        if(map.getOverlays().contains(direction_markers)){

        }
        map.getOverlays().removeAll(direction_markers);

        map.getOverlays().addAll(direction_arrows);*/

        /*// update total km
        tv_info_map.setVisibility(View.VISIBLE);
        String st_total_distance = "-- Km";
        if(distance != null){
            st_total_distance = distance + " Km";
        }
        tv_info_map.setText(st_total_distance);*/

        map.invalidate();
    }

    public void removeTrack(int position){

        if(polylines != null){
            map.getOverlays().remove(polylines.get(position));
            polylines.remove(position);
        }

        if(direction_markers != null){
            map.getOverlays().removeAll(direction_markers.get(position));
            direction_markers.remove(position);
        }

        //refreshTrack();
        map.invalidate();
    }


    /**
     * get all the points that forms today path and draw it in the map
     */
    private void drawTodayPath() {

        Toast.makeText(getActivity(), "Load today path in progress ...", Toast.LENGTH_SHORT).show();

       /* final ProgressDialog progressDialog;

        tracks = new ArrayList<>();
        direction_arrows = new ArrayList<>();
        waypoints = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.menu_today_route));
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setIcon(R.drawable.ic_address);
        progressDialog.setCancelable(false);

        *//*progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(way_points_call != null){
                    if(way_points_call.isExecuted()){
                        way_points_call.cancel();
                        if(getView() != null){
                            switch_load_todayPath.setChecked(false);
                        }
                    }
                }
                Toast.makeText(getContext(), getString(R.string.today_path_cancelled), Toast.LENGTH_SHORT).show();
            }
        });*//*

        if(Methods.isNetworkAvailable(getContext())){

            progressDialog.show();

            way_points_call = wayPointsController.getItinerary(currentTrackingDynData.getTracking_item_id(), Constants.REFERENCE.TODAY);

            way_points_call.enqueue(new Callback<TodayPath>() {
                @Override
                public void onResponse(Call<TodayPath> call, Response<TodayPath> response) {
                    Log.i("Retrofit", "Success" + " " + call.request().toString());

                    TodayPath todayPath = response.body();

                    if(todayPath != null){
                        List<Waypoint> way_points = todayPath.getWaypoints();

                        if(way_points != null){

                            // global variable
                            waypoints = way_points;
                            // add geopoints
                            for (int i=0; i<way_points.size(); i++) {
                                if(way_points.get(i) != null){
                                    if(way_points.get(i).getGeopoint() != null){
                                        // add this point
                                        geopoints.add(way_points.get(i).getGeopoint());
                                    }
                                }
                            }

                            pathOverlay = new Polyline();

                            if(geopoints.isEmpty()) {//|| todayPath.getLast_today_distance_counter() == 0){
                                Toast.makeText(getContext(), getString(R.string.today_path_not_found), Toast.LENGTH_LONG).show();
                                switch_load_todayPath.setChecked(false);
                            }
                            else{
                                // get all points to add a pathOverlay to the map
                                pathOverlay.setPoints(geopoints);

                                // add path directions
                                for(int i=0; i< geopoints.size(); i++){

                                    if(i%10 == 0){
                                        Marker marker = new Marker(map);
                                        marker.setPosition(waypoints.get(i).getGeopoint());
                                        marker.setRotation(waypoints.get(i).getCourse());
                                        marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_direction_arrow));
                                        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                                return true;
                                            }
                                        });
                                        direction_arrows.add(marker);
                                    }
                                }
                                map.getOverlays().addAll(direction_arrows);


                                // update total km
                                tv_info_map.setVisibility(View.VISIBLE);
                                String st_total_distance = "-- Km";
                                if(todayPath.getLast_today_distance_counter() != null){
                                    st_total_distance = todayPath.getLast_today_distance_counter() + " Km";
                                }
                                tv_info_map.setText(st_total_distance);
                                // add start marker

                                today_path_start_marker = new Marker(map);
                                today_path_start_marker.setPosition(geopoints.get(0));
                                today_path_start_marker.setTitle(getString(R.string.first_activity) + ": " + currentTrackingDynData.getToday_first_activity_Time(getContext()));
                                map.getOverlays().add(today_path_start_marker);

                                // add the route to the map smoothly with this function
                                drawRoute(getContext(), map);

                                map.invalidate();

                                smoothPathScrollListener = new MapListener() {
                                    @Override
                                    public boolean onScroll(ScrollEvent event) {
                                        drawRoute(getActivity(), map);
                                        return false;
                                    }

                                    @Override
                                    public boolean onZoom(ZoomEvent event) {
                                        drawRoute(getActivity(), map);
                                        //Toast.makeText(getContext(), "Zoom: " + event.getZoomLevel() , Toast.LENGTH_SHORT).show();
                                        //updateDirectionMarker(event.getZoomLevel());
                                        return false;
                                    }
                                };

                                // map listener
                                map.setMapListener(smoothPathScrollListener);

                                map.invalidate();
                            }


                        }
                        else{
                            Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                        }

                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<TodayPath> call, Throwable t) {

                    if(way_points_call.isCanceled()){
                        Log.i("Retrofit", "Cancelled");
                    }
                    else{
                        Log.i("Retrofit", "Failure" + " " + call.request().toString() + " " + t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
        else{
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
            switch_load_today_stops.setChecked(false);
        }*/

    }

    /**
     * This method removes today path from the map
     */
    private void removeTodayPath(){
        if(pathOverlay != null){

            if(updateThread != null){
                // interrupt the thread from drawing route when scrolling
                updateThread.interrupt();
            }
            // remove listener on scroll and zoom
            map.setMapListener(null);
            // clear list if points
            pathOverlay.getPoints().clear();
            // remove path overlay
            map.getOverlays().remove(pathOverlay);
            // remove direction arrows
            //map.getOverlays().removeAll(direction_arrows);
            // remove start point
            //map.getOverlays().remove(today_path_start_marker);
            // hide info path
            tv_info_map.setVisibility(View.GONE);
            // validate changes and reload the map
            map.invalidate();

        }
    }


    /**
     * Get all today stops and draw it on the map
     */
    private void drawTodayStops(){

        /*final ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.menu_today_stops));
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setIcon(R.drawable.ic_stop_marker);
        progressDialog.setCancelable(false);

        *//*progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(way_points_call != null){
                    if(way_points_call.isExecuted()){
                        way_points_call.cancel();
                        removeTodayStops();
                        switch_load_today_stops.setChecked(false);
                    }
                }
                Toast.makeText(getContext(), getString(R.string.today_stops_cancelled), Toast.LENGTH_SHORT).show();
            }
        });*//*

        if(Methods.isNetworkAvailable(getContext())){

            progressDialog.show();

            stops_call = stopsController.getTodayStops(currentTrackingDynData.getTracking_item_id());

            stops_call.enqueue(new Callback<Stop>() {
                @Override
                public void onResponse(Call<Stop> call, Response<Stop> response) {
                    Log.i("Retrofit", "Success" + " " + call.request().toString());

                    Stop stop = response.body();

                    if(stop != null){

                        stop_markers.clear();

                        for (StopData stopData : stop.getData()) {

                            if(stopData != null){

                                if(stopData.getGeopoint() != null){

                                    Marker marker = new Marker(map);
                                    marker.setPosition(stopData.getGeopoint());
                                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop_marker));
                                    InfoWindow custInfoWindow_stops = new InfoWindow(R.layout.custom_info_window_onclick_stops, map) {
                                        @Override
                                        public void onOpen(Object item) {
                                            InfoWindow.closeAllInfoWindowsOn(map);
                                        }

                                        @Override
                                        public void onClose() {
                                            InfoWindow.closeAllInfoWindowsOn(map);
                                        }
                                    };

                                    configureCustomInfoWindow_vehicle_stops(stopData, custInfoWindow_stops);

                                    marker.setInfoWindow(custInfoWindow_stops);

                                    // add those markers to the map
                                    stop_markers.add(marker);

                                }
                            }
                        }
                        if(stop_markers.isEmpty()){
                            Toast.makeText(getContext(), getString(R.string.today_stops_not_found), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            map.getOverlays().addAll(stop_markers);
                            map.getOverlays().remove(vehicleMarker);
                            map.getOverlays().add(vehicleMarker);

                            if(stop_markers.get(0) != null){
                                map.getController().animateTo(stop_markers.get(0).getPosition());
                            }

                            // refresh the map
                            map.invalidate();

                        }

                    }
                    else{
                        Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }


                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Stop> call, Throwable t) {
                    if(stops_call.isCanceled()){
                        Log.i("Retrofit", "Cancelled");
                    }
                    else{
                        Log.i("Retrofit", "Failure" + " " + call.request().toString() + " " + t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
            switch_load_today_stops.setChecked(false);
        }
        */
    }

    /**
     * Remove today stops from the map
     */
    public void removeTodayStops(){
        if(stop_markers != null){
            if(!stop_markers.isEmpty()){
                InfoWindow.closeAllInfoWindowsOn(map);
                map.getOverlays().removeAll(stop_markers);
                map.invalidate();
            }
        }
    }


    /**
     * Configuring the map
     */
    public void configureMap(){

        if(map != null){

            // set the zoom and the center odf the map
            map.getController().setZoom(16);

            // get the Map API
            mapService = mapHelper.getConfigRetrofitCall();
            if(mapService != null){
                // configure the map
                mapHelper.configureMap(mapService);
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }


    // **************************** Draw route smoothly ******************************

    /**
     * Draw pathOverlay
     * @param context
     * @param osmMap
     */
    public void drawRoute(final Context context, final MapView osmMap){
        if(updateThread == null || !updateThread.isAlive()){
            //updateRoute(context, osmMap);
        }
    }

    /**
     * Update path overlay
     * @param context
     * @param osmMap
     */
    /*private void updateRoute(final Context context, final MapView osmMap){
        updateThread = new Thread(new Runnable() {
            public void run() {
                // make copy of lists
                final ArrayList<GeoPoint> zoomPoints = new ArrayList<GeoPoint>(geopoints);
                final ArrayList<Marker> directionPoints = new ArrayList<Marker>(direction_arrows);
                //Remove any points that are offscreen
                removeHiddenPoints(osmMap, zoomPoints, directionPoints);

                //If there's still too many then thin the array
                if(zoomPoints.size() > MAX_POINTS){
                    int stepSize = (int) zoomPoints.size()/MAX_POINTS;
                    int count = 1;
                    for (Iterator<GeoPoint> iterator = zoomPoints.iterator(); iterator.hasNext();) {
                        iterator.next();

                        if(count != stepSize){
                            iterator.remove();
                        }else{
                            count = 0;
                        }

                        count++;
                    }
                }


                //If there's still too many then thin the array (max 20 direction arrow on the map)
                int value = 20;
                if(directionPoints.size() > value){
                    int stepSize = (int) directionPoints.size()/value;
                    int count = 1;
                    for (Iterator<Marker> iterator = directionPoints.iterator(); iterator.hasNext();) {
                        iterator.next();

                        if(count != stepSize){
                            iterator.remove();
                        }else{
                            count = 0;
                        }

                        count++;
                    }
                }

                //Update the map on the event thread
                osmMap.post(new Runnable() {
                    public void run() {
                        //ideally the Polyline construction would happen in the thread but that causes glitches while the event thread
                        //waits for redraw:

                        // remove vehicle marker
                        osmMap.getOverlays().remove(vehicleMarker);

                        // add the path
                        osmMap.getOverlays().remove(pathOverlay);
                        pathOverlay = new Polyline();
                        pathOverlay.setPoints(zoomPoints);
                        pathOverlay.setColor(Color.BLACK);
                        osmMap.getOverlays().add(pathOverlay);
                        osmMap.invalidate();

                        // add direction arrrows
                        osmMap.getOverlays().removeAll(direction_arrows);
                        osmMap.getOverlays().addAll(directionPoints);

                        // add vehicle marker in order to place it on the top
                        osmMap.getOverlays().add(vehicleMarker);

                    }
                });
            }
        });
        updateThread.start();
    }*/

    /**
     * Remove hidden points to make smooth scroll on the map
     * @param osmMap
     * @param zoomPoints
     * @param directionPoints
     */
    private void removeHiddenPoints(MapView osmMap, ArrayList<GeoPoint> zoomPoints, ArrayList<Marker> directionPoints){
       if(osmMap != null){

           BoundingBoxE6 bounds = osmMap.getBoundingBoxE6();


           for (Iterator<GeoPoint> iterator = zoomPoints.iterator(); iterator.hasNext();) {

               GeoPoint point = iterator.next();

               if(!isShownOnMap(osmMap, point, bounds)){

                   if(iterator.hasNext()){

                       GeoPoint following_point = iterator.next();

                       if(!isShownOnMap(osmMap, following_point, bounds)){
                           iterator.remove();
                           //zoomPoints.remove(following_point);
                       }
                   }

               }
           }

           for (Iterator<Marker> iterator = directionPoints.iterator(); iterator.hasNext();) {

               GeoPoint point = iterator.next().getPosition();

                if(!isShownOnMap(osmMap, point, bounds)){
                    iterator.remove();
                }
           }
       }

    }

   // Returns if the given points is shown in the screen
    public boolean isShownOnMap(MapView osmMap, GeoPoint point, BoundingBoxE6 bounds){
        if(osmMap != null){

            boolean inLongitude = point.getLatitudeE6() < bounds.getLatNorthE6() && point.getLatitudeE6() > bounds.getLatSouthE6();
            boolean inLatitude = point.getLongitudeE6() > bounds.getLonWestE6() && point.getLongitudeE6() < bounds.getLonEastE6();

            if(!inLongitude || !inLatitude){
                return  false;
            }
        }

        return true;
    }


}
