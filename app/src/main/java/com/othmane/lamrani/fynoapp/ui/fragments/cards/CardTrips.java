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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.Stop;
import com.othmane.lamrani.fynoapp.API.models.StopData;
import com.othmane.lamrani.fynoapp.API.models.Trip;
import com.othmane.lamrani.fynoapp.API.models.TripResponse;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.adapter.StopsAdapter;
import com.othmane.lamrani.fynoapp.adapter.TripsAdapter;
import com.othmane.lamrani.fynoapp.controller.StopsController;
import com.othmane.lamrani.fynoapp.controller.WayPointsController;
import com.othmane.lamrani.fynoapp.helper.Constants;
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

public class CardTrips extends Fragment {

    private LinearLayout tv_load_vehicle_trips;
    private RecyclerView recyclerViewTrips;
    private List<Trip> tripsList;
    private TripsAdapter tripsAdapter;
    private Call<TripResponse> trips_call;
    public WayPointsController wayPointsController;
    private Calendar myCalendar;
    private LinearLayout btn_choose_day;
    private TextView tv_trips_date;
    private DatePickerDialog.OnDateSetListener date;
    private CheckBox checkBox_select_all;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.card_vehicle_trips, container, false);

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

                String date = dayOfMonth + "-" + month + "-" + year;
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
        tv_load_vehicle_trips = (LinearLayout) getView().findViewById(R.id.tv_load_vehicle_trips);
        wayPointsController = new WayPointsController();
        btn_choose_day = getView().findViewById(R.id.btn_choose_day);
        tv_trips_date =  getView().findViewById(R.id.tv_trip_date);
        checkBox_select_all = getView().findViewById(R.id.check_select_all);

        checkBox_select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    adAllTracks();
                }
                else{
                    removeAllTracks();
                }
            }
        });
    }

    private void removeAllTracks() {
        tripsAdapter.removeAllTracks();
    }

    private void adAllTracks() {
        tripsAdapter.drawAllTracks();
    }


    /**
     * Configuring the recycler view and its adapter
     */
    private void configureRecyclerViewForStops() {

        tripsList = new ArrayList<>();

        recyclerViewTrips = (RecyclerView) getView().findViewById(R.id.tripsRecyclerView);
        // To make scrolling smoothly
        recyclerViewTrips.setNestedScrollingEnabled(false);
        recyclerViewTrips.setHasFixedSize(true);
        recyclerViewTrips.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));

        tripsAdapter = new TripsAdapter((MainActivity) getActivity());

        recyclerViewTrips.setAdapter(tripsAdapter);

    }

    /**
     * Update stop date label
     */
    public void update_label_stop_date(String date){
        if(date != null){
            tv_trips_date.setText(date);
        }
    }

    /**
     * Load vehicle stops
     */
    public void load_vehicle_stops(String date){
        if(Methods.isNetworkAvailable(getContext())){

            tripsAdapter.removeAllItems();
            tripsList.clear();

            tv_load_vehicle_trips.setVisibility(View.VISIBLE);
            checkBox_select_all.setVisibility(View.GONE);

            if(date != null){
                trips_call = wayPointsController.getTrips(FragmentEquipmentDetails.currentTrackingDynData.getTracking_item_id(), date);
            }
            else {
                trips_call = wayPointsController.getTrips(FragmentEquipmentDetails.currentTrackingDynData.getTracking_item_id(), Constants.REFERENCE.TODAY);

            }

            trips_call.enqueue(new Callback<TripResponse>() {
                @Override
                public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                    Log.i("Retrofit", "Success" + " " + call.request().toString());


                    TripResponse tripResponse = response.body();

                    if(tripResponse != null){
                        if(tripResponse.getTrips() != null){
                            tripsList = tripResponse.getTrips();
                            for(Trip trip : tripsList){
                                tripsAdapter.addItem(trip);
                            }

                            //FragmentEquipmentDetails fragmentEquipmentDetails = (FragmentEquipmentDetails) getActivity().getSupportFragmentManager().findFragmentByTag(FragmentEquipmentDetails.class.getName());
                            //fragmentEquipmentDetails.direction_markers = new ArrayList<>(tripsList.size());
                            //fragmentEquipmentDetails.polylines = new ArrayList<>(tripsList.size());
                            //fragmentEquipmentDetails.stop_markers = new ArrayList<>(tripsList.size());

                            if(!tripsList.isEmpty()){
                                checkBox_select_all.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(getActivity(), "Aucun trip trouvé", Toast.LENGTH_SHORT).show();
                            }

                        }

                        tv_load_vehicle_trips.setVisibility(View.GONE);

                    }
                    else{
                        Toast.makeText(getContext(), getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TripResponse> call, Throwable t) {
                    Log.i("Retrofit", "Failure" + " " + call.request().toString() + " " + t.getMessage());

                    if(trips_call.isCanceled()){
                        Log.i("Retrofit", getString(R.string.today_stops_cancelled));
                    }
                    else{
                        Log.i("Retrofit", getString(R.string.intern_error));
                    }
                }
            });


        }
        else{
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
        }
    }



}
