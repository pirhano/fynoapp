package com.othmane.lamrani.fynoapp.ui.fragments.cards;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipmentDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class CardStops extends Fragment {

    private LinearLayout tv_load_vehicle_stops;
    private RecyclerView recyclerViewStops;
    private List<StopData> stopsList;
    private StopsAdapter stopsAdapter;
    private Call<Stop> stops_call;
    public StopsController stopsController;
    private Calendar myCalendar;
    private LinearLayout btn_choose_day;
    private TextView tv_stop_date;
    private DatePickerDialog.OnDateSetListener date;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.card_vehicle_stops, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configView();


        // configure recycler view
        configureRecyclerViewForStops();

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

                String date = dayOfMonth + "_" + month + "_" + year;
                update_label_stop_date(dayOfMonth + "/" + month + "/" + year);
                load_vehicle_stops(date);
            }

        };

        load_vehicle_stops(null);


    }

    /**
     * Configure the UI components
     */
    private void configView() {
        tv_load_vehicle_stops = (LinearLayout) getView().findViewById(R.id.tv_load_vehicle_stops);
        stopsController = new StopsController();
        btn_choose_day = getView().findViewById(R.id.btn_choose_day);
        tv_stop_date =  getView().findViewById(R.id.tv_stop_date);
    }


    /**
     * Configuring the recycler view and its adapter
     */
    private void configureRecyclerViewForStops() {

        stopsList = new ArrayList<>();

        recyclerViewStops = (RecyclerView) getView().findViewById(R.id.stopsRecyclerView);
        // To make scrolling smoothly
        recyclerViewStops.setNestedScrollingEnabled(false);
        recyclerViewStops.setHasFixedSize(true);
        recyclerViewStops.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerViewStops.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));

        stopsAdapter = new StopsAdapter((MainActivity) getActivity());

        recyclerViewStops.setAdapter(stopsAdapter);

    }

    /**
     * Update stop date label
     */
    public void update_label_stop_date(String date){
        if(date != null){
            tv_stop_date.setText(date);
        }
    }

    /**
     * Load vehicle stops
     */
    public void load_vehicle_stops(String date){
        if(Methods.isNetworkAvailable(getContext())){

            stopsAdapter.removeAllItems();
            stopsList.clear();

            tv_load_vehicle_stops.setVisibility(View.VISIBLE);

            if(date != null){
                stops_call = stopsController.getAllStops(FragmentEquipmentDetails.currentTrackingDynData.getTracking_item_id(), date);
            }
            else {
                stops_call = stopsController.getTodayStops(FragmentEquipmentDetails.currentTrackingDynData.getTracking_item_id());
            }

            stops_call.enqueue(new Callback<Stop>() {
                @Override
                public void onResponse(Call<Stop> call, Response<Stop> response) {
                    Log.i("Retrofit", "Success" + " " + call.request().toString());

                    // response
                    Stop stop = response.body();

                    if(stop != null){
                        stopsList = stop.getData();
                        for (StopData stop_data : stopsList) {
                            if(stop != null){
                                stopsAdapter.addStop(stop_data);
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
                        Log.i("Retrofit", getString(R.string.today_stops_cancelled));
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
