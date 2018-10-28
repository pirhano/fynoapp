package com.othmane.lamrani.fynoapp.ui.fragments.Tabs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.callback.MapFeaturesAPI;
import com.othmane.lamrani.fynoapp.API.models.StopData;
import com.othmane.lamrani.fynoapp.API.models.TodayPath;
import com.othmane.lamrani.fynoapp.API.models.Waypoint;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.adapter.StopsAdapter;
import com.othmane.lamrani.fynoapp.controller.WayPointsController;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.helper.RequestPermission;
import com.othmane.lamrani.fynoapp.helper.osmdroid.MapHelper;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentHistory;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class TabHistoryItinerary extends RequestPermission {

    public static MapView map;
    private MapFeaturesAPI service;
    private MapHelper mapHelper;
    //public static ProgressBar progressBar;
    private static List<Marker> trackingDataList;
    private ImageButton btn_map_menu;
    private LinearLayout container_itinerary_lists;
    private boolean menu_opened = false;
    private View dark_bg;

    private StopsAdapter stopsAdapter;
    private RecyclerView recyclerViewStops;
    private List<StopData> itinerariesList;
    private LinearLayout tv_load_vehicle_itineraries;
    public static Call<TodayPath> itineraries_call;
    public WayPointsController waypointsController;
    private LinearLayout btn_choose_day;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;
    private String selected_date;
    private ImageButton btn_refresh;
    private Polyline polyline;
    private TextView tv_itinerary_date_map, tv_itinerary_date_list, tv_ifo_itinerary;
    public Marker itinerary_start_marker, itinerary_end_marker;
    private int id_tracking_item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab_history_itinerary, container, false);

        return rootView;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        menu_opened = false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        id_tracking_item = FragmentHistory.currentTrackingDynData.getTracking_item_id();

        configureView();

        waypointsController = new WayPointsController();

        // configure the map
        configureView();

        // when the user clicks on the button to chose a date in order to load stops
        btn_choose_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                int month = monthOfYear + 1;

                selected_date = dayOfMonth + "_" + month + "_" + year;
                update_label_stop_date(dayOfMonth + "/" + month + "/" + year);

                // directly draw
                drawItineraryForSelectedDate();
            }

        };

        // Configure btn for inflating menu
        btn_map_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*//Toast.makeText(getContext(), "Clicked option menu", Toast.LENGTH_SHORT).show();
                if(menu_opened){
                    hide_info_card();
                }
                else{
                    InfoWindow.closeAllInfoWindowsOn(map);
                    show_info_card();
                }*/
            }
        });

        tv_itinerary_date_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_ifo_itinerary.setVisibility(View.GONE);
                //tv_itinerary_date_map.setVisibility(View.GONE);

                if(polyline != null){
                    map.getOverlays().remove(polyline);
                }
                if(itinerary_start_marker != null){
                    map.getOverlays().remove(itinerary_start_marker);
                }
                if(itinerary_end_marker != null){
                    map.getOverlays().remove(itinerary_end_marker);
                }

                map.invalidate();

                // draw today path
                drawItineraryForSelectedDate();
            }
        });

        // draw today path
        drawItineraryForSelectedDate();

    }

    private void configureView() {

        configureMap();

        // show loading message and progress bar
        tv_load_vehicle_itineraries = (LinearLayout) getView().findViewById(R.id.tv_load_vehicle_itineraries);
        btn_choose_day = (LinearLayout) getView().findViewById(R.id.btn_choose_day);
        tv_itinerary_date_list = (TextView) getView().findViewById(R.id.tv_itinerary_date_list);
        tv_itinerary_date_map = (TextView) getView().findViewById(R.id.tv_itinerary_date_map);
        // refresh button
        btn_refresh = MainActivity.toolbar.findViewById(R.id.btn_refresh);

        // progress bar
        //progressBar = (ProgressBar) getView().findViewById(R.id.progressbar);
        // map menu
        btn_map_menu = (ImageButton) getView().findViewById(R.id.btn_map_menu);
        // container lists
        container_itinerary_lists = (LinearLayout) getView().findViewById(R.id.container_itinerary_lists);
        // dark bg
        dark_bg = getView().findViewById(R.id.bg_dark);
        // tv
        tv_ifo_itinerary = getView().findViewById(R.id.tv_info_itinerary);
        tv_itinerary_date_map = getView().findViewById(R.id.tv_itinerary_date_map);
        tv_itinerary_date_list = getView().findViewById(R.id.tv_itinerary_date_list);
    }


    public void configureMap(){

        // Map container
        map = (MapView) getView().findViewById(R.id.map);

        // Map helper
        mapHelper = new MapHelper(getContext(), map);

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

    // show card info
    public void show_info_card(){
        if(container_itinerary_lists != null){

            dark_bg.setVisibility(View.VISIBLE);

            container_itinerary_lists.setVisibility(View.VISIBLE);

            // Slide the container from the bottom of the screen
            container_itinerary_lists.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_1s));

            menu_opened = true;

            // disable clicks on buttons
            disable_click_on_map_controls();

            // disable clicks on the black view
            dark_bg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hide_info_card();
                    return true;
                }
            });

        }
        else{
            Toast.makeText(getContext(), "cannot find layout", Toast.LENGTH_SHORT).show();
        }
    }

    // hide card info
    public void hide_info_card(){
        if(container_itinerary_lists != null){
            dark_bg.setVisibility(View.GONE);
            container_itinerary_lists.setVisibility(View.GONE);
            // Slide the container from the bottom of the screen
            //layout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_1s));
            menu_opened = false;

            // allow clicks on map controls
            allow_click_on_map_controls();
        }
        else{
            Toast.makeText(getContext(), "cannot find layout", Toast.LENGTH_SHORT).show();
        }
    }

    // allow clicks on map controls
    public void allow_click_on_map_controls(){
        btn_map_menu.setEnabled(true);
    }

    // disable clicks on map controls
    public void disable_click_on_map_controls(){
        btn_map_menu.setEnabled(false);
    }


    /**
     * Update stop date label
     */
    public void update_label_stop_date(String st_date){
        if(st_date != null){
            tv_itinerary_date_list.setText(st_date);
            tv_itinerary_date_map.setText(st_date);
        }
        else{
            tv_itinerary_date_list.setText(getString(R.string.today));
            tv_itinerary_date_map.setText(getString(R.string.today));
        }
    }



    /**
     * get all the points that forms today path and draw it in the map
     */
    private void drawItineraryForSelectedDate() {

        tv_ifo_itinerary.setVisibility(View.GONE);
        //tv_itinerary_date_map.setVisibility(View.GONE);

        if(polyline != null){
            map.getOverlays().remove(polyline);
        }
        if(itinerary_start_marker != null){
            map.getOverlays().remove(itinerary_start_marker);
        }
        if(itinerary_end_marker != null){
            map.getOverlays().remove(itinerary_end_marker);
        }

        map.invalidate();

        final ProgressDialog progressDialog;
        final List<GeoPoint> geopoints;

        geopoints = new ArrayList<>();

        progressDialog = new ProgressDialog(getView().getContext());
        progressDialog.setTitle(getString(R.string.menu_today_route));
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setIcon(R.drawable.ic_address);
        progressDialog.setCancelable(true);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(itineraries_call != null){
                    if(itineraries_call.isExecuted()){
                        itineraries_call.cancel();
                    }
                }
                Toast.makeText(getContext(), getString(R.string.today_path_cancelled), Toast.LENGTH_SHORT).show();
            }
        });

        if(Methods.isNetworkAvailable(getContext())){

            if(selected_date != null){
                progressDialog.show();
                itineraries_call = waypointsController.getItinerary(id_tracking_item, selected_date);
            }
            else{
                itineraries_call = waypointsController.getItinerary(id_tracking_item, Constants.REFERENCE.TODAY);
            }

            itineraries_call.enqueue(new Callback<TodayPath>() {
                @Override
                public void onResponse(Call<TodayPath> call, Response<TodayPath> response) {
                    Log.i("Retrofit", "Success" + " " + call.request().toString());

                    TodayPath todayPath = response.body();

                    if(todayPath != null){
                        List<Waypoint> way_points = todayPath.getWaypoints();

                        if(way_points != null){
                            for (Waypoint way_point: way_points) {
                                if(way_point != null){
                                    if(way_point.getGeopoint() != null){
                                        geopoints.add(way_point.getGeopoint());
                                    }
                                }
                            }


                            polyline = new Polyline();
                            if(geopoints.isEmpty()){
                                Toast.makeText(getContext(), getString(R.string.today_path_not_found), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // get all points to add a pathOverlay to the map
                                polyline.setPoints(geopoints);
                                map.getOverlayManager().add(polyline);
                                // update total km
                                tv_ifo_itinerary.setVisibility(View.GONE);
                                tv_itinerary_date_map.setVisibility(View.VISIBLE);
                                String st_total_distance = "-- Km";
                                if(todayPath.getLast_today_distance_counter() != null){
                                    st_total_distance = todayPath.getLast_today_distance_counter() + " Km";
                                }
                                tv_ifo_itinerary.setText(st_total_distance);

                                // add start marker
                                itinerary_start_marker = new Marker(map);
                                itinerary_start_marker.setPosition(geopoints.get(0));
                                itinerary_start_marker.setTitle(getString(R.string.first_activity) + ": " + FragmentHistory.currentTrackingDynData.getToday_first_activity_Time(getContext()));

                                itinerary_end_marker = new Marker(map);
                                itinerary_end_marker.setPosition(geopoints.get(geopoints.size()-1));
                                itinerary_end_marker.setTitle("Point d'arrivée");

                                // add it to the map
                                map.getOverlays().add(itinerary_start_marker);
                                map.getOverlays().add(itinerary_end_marker);

                                map.getController().setZoom(12);

                                map.getController().animateTo(geopoints.get(0));
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

                    if(itineraries_call.isCanceled()){
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
        }

    }


    @Override
    public void onPermissionsGranted(int requestCode) {

    }
}
