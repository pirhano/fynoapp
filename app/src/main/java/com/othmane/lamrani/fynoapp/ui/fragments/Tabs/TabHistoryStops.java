package com.othmane.lamrani.fynoapp.ui.fragments.Tabs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.Stop;
import com.othmane.lamrani.fynoapp.API.models.StopData;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.adapter.StopsAdapter;
import com.othmane.lamrani.fynoapp.controller.StopsController;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentHistory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class TabHistoryStops extends Fragment {

    private StopsAdapter stopsAdapter;
    private RecyclerView recyclerViewStops;
    private List<StopData> stopsList;
    private LinearLayout tv_load_vehicle_stops;
    public static Call<Stop> stops_call;
    private int id_tracking_item;
    public StopsController stopsController;
    private LinearLayout btn_choose_day;
    private TextView tv_stop_date;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;
    private String selected_date;
    private ImageButton btn_refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab_history_stops, container, false);

        return rootView;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        id_tracking_item = FragmentHistory.currentTrackingDynData.getTracking_item_id();

        if(getView() != null){
            configView();
        }

        // getting the equipment passed
        if(FragmentHistory.currentTrackingDynData != null){

            stopsController = new StopsController();

            // load today vehicle stops
            load_vehicle_stops(null);

            // configure refresh button
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // refresh the listView
                    load_vehicle_stops(selected_date);
                }
            });

        }
        else{
            Snackbar.make(getView(), getString(R.string.intern_error), Snackbar.LENGTH_INDEFINITE).show();
            tv_load_vehicle_stops.setVisibility(View.GONE);
        }


    }

    private void configView() {

        // show loading message and progress bar
        tv_load_vehicle_stops = (LinearLayout) getView().findViewById(R.id.tv_load_vehicle_stops);
        btn_choose_day = (LinearLayout) getView().findViewById(R.id.btn_choose_day);
        tv_stop_date = (TextView) getView().findViewById(R.id.tv_stop_date);

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
                load_vehicle_stops(selected_date);
            }

        };


        // list that contains all the stops
        stopsList = new ArrayList<>();

        recyclerViewStops = (RecyclerView) getView().findViewById(R.id.stopsRecyclerView);
        // To make scrolling smoothly
        recyclerViewStops.setNestedScrollingEnabled(false);
        recyclerViewStops.setHasFixedSize(true);
        recyclerViewStops.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerViewStops.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        stopsAdapter = new StopsAdapter((MainActivity) getActivity());

        recyclerViewStops.setAdapter(stopsAdapter);

        // refresh button
        btn_refresh = MainActivity.toolbar.findViewById(R.id.btn_refresh);

    }

    /**
     * Update stop date label
     */
    public void update_label_stop_date(String st_date){
        if(st_date != null){
            tv_stop_date.setText(st_date);
        }
    }

    /**
     * Load vehicle stops
     */
    public void load_vehicle_stops(String date){
        if(Methods.isNetworkAvailable(getContext())){

            // remove all previous items
            stopsAdapter.removeAllItems();
            stopsList.clear();

            tv_load_vehicle_stops.setVisibility(View.VISIBLE);

            if(date != null){
                stops_call = stopsController.getAllStops(id_tracking_item, date);
            }
            else {
                stops_call = stopsController.getTodayStops(id_tracking_item);
            }

            stops_call.enqueue(new Callback<Stop>() {
                @Override
                public void onResponse(Call<Stop> call, Response<Stop> response) {
                    Log.i("Retrofit", "Success" + " " + call.request().toString());

                    Stop stop = response.body();

                    if(stop != null){
                        stopsList = stop.getData();
                        for (StopData stopData : stopsList) {
                            if(stopData != null){
                                stopsAdapter.addStop(stopData);
                            }
                        }

                        tv_load_vehicle_stops.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Stop> call, Throwable t) {
                    Log.i("Retrofit", "Failure" + " " + call.request().toString() + " " + t.getMessage());

                    if(stops_call.isCanceled()){
                        Log.i("Retrofit", "Cancelled");
                    }
                    else{
                        Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
        }
    }

}
