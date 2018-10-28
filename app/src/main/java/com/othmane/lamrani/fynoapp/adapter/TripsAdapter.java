package com.othmane.lamrani.fynoapp.adapter;


import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.OperatingApiClient;
import com.othmane.lamrani.fynoapp.API.models.StopData;
import com.othmane.lamrani.fynoapp.API.models.TrackCoordinates;
import com.othmane.lamrani.fynoapp.API.models.Trip;
import com.othmane.lamrani.fynoapp.API.models.TripStops;
import com.othmane.lamrani.fynoapp.API.models.TripTrackInfo;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.controller.WayPointsController;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipmentDetails;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lamrani on 21/09/2017.
 */

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.Holder>{

    private List<Trip> mTripData;
    private MainActivity activity;

    // use equipement instead of tracking dyn data

    public TripsAdapter(MainActivity activity){
        mTripData = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        Trip trip = mTripData.get(position);

        String date = (trip.getStart_timestamp_srv() != null) ? Methods.getTimeOnly(activity, trip.getStart_timestamp_srv()) : "--";
        if(trip.getEnd_timstamp_srv() != null){
            date = date + " - " + Methods.getTimeOnly(activity, trip.getEnd_timstamp_srv());
        }

        String max_speed = trip.getMax_speed();

        String distance = (trip.getDiff_distance_counter() != null) ? String.valueOf(trip.getDiff_distance_counter()) : "--";


        holder.tv_trip_date.setText(date);
        holder.tv_trip_max_speed.setText(max_speed + " Km/h");
        holder.tv_trip_distance.setText(distance  + " Km");

        holder.check_trip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = holder.getAdapterPosition();

                if(b){
                    drawTrip(position);
                }
                else{
                    removeTrip(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTripData.size();
    }

    public void addItem(Trip trip) {
        mTripData.add(trip);
        notifyDataSetChanged();
    }

    public void removeAllItems(){
        mTripData.clear();
        notifyDataSetChanged();
    }

    /**
     *
     * @param position
     * @return
     */
    public Trip getSelectedStop(int position) {
        return mTripData.get(position);
    }


    public void drawAllTracks() {
        /*for(int i=0; i<mTripData.size(); i++){
            drawTrip(i);
        }*/
        RecyclerView recyclerview = activity.findViewById(R.id.tripsRecyclerView);
        for(int i=0; i < recyclerview.getChildCount(); i++){
            LinearLayout itemLayout = (LinearLayout) recyclerview.getChildAt(i);
            CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.check_trip);
            cb.setChecked(true);
        }
    }

    public void removeAllTracks() {
        RecyclerView recyclerview = activity.findViewById(R.id.tripsRecyclerView);
        for(int i=0; i < recyclerview.getChildCount(); i++){
            LinearLayout itemLayout = (LinearLayout) recyclerview.getChildAt(i);
            CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.check_trip);
            cb.setChecked(false);
        }
    }

    public void removeTrip(int position){
        FragmentEquipmentDetails fragmentEquipmentDetails = (FragmentEquipmentDetails) activity.getSupportFragmentManager().findFragmentByTag(FragmentEquipmentDetails.class.getName());
        fragmentEquipmentDetails.removeTrack(position);
    }

    /**
     *
     * @param position: position of the current selceted item
     */
    public void drawTrip(final int position){

        final Trip trip = mTripData.get(position);

        final long id_trip = trip.getId();

        WayPointsController wayPointsController = new WayPointsController();

        Call<List<TripTrackInfo>> call_track = wayPointsController.getTrip(id_trip);
        call_track.enqueue(new Callback<List<TripTrackInfo>>() {
            @Override
            public void onResponse(Call<List<TripTrackInfo>> call, Response<List<TripTrackInfo>> response) {
                Log.i("Retrofit", "Success" + " " + call.request().toString());

                List<TripTrackInfo> tripTrackInfo = response.body();
                if(tripTrackInfo != null){
                    TrackCoordinates trackCoordinates = tripTrackInfo.get(0).getTrack();

                    if(trackCoordinates != null){
                        List<GeoPoint> geopoints = trackCoordinates.getGeopoints();
                        if(geopoints != null){
                            //Toast.makeText(activity, geopoints.size() + " items", Toast.LENGTH_SHORT).show();

                            FragmentEquipmentDetails fragmentEquipmentDetails = (FragmentEquipmentDetails) activity.getSupportFragmentManager().findFragmentByTag(FragmentEquipmentDetails.class.getName());

                            // get stops
                            List<TripStops> tripStops = tripTrackInfo.get(0).getStops();

                            // double distance = mTripData.get(position).getDiff_distance_counter()
                            fragmentEquipmentDetails.drawTrack(position, geopoints, tripTrackInfo.get(0).getPoint_courses(), tripStops);

                        }
                        else {
                            Toast.makeText(activity, activity.getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(activity, activity.getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(activity, activity.getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripTrackInfo>> call, Throwable t) {
                Log.i("Retrofit", "Failure" + " " + call.request().toString() + " " + t.getMessage());

                Toast.makeText(activity, activity.getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
            }
        });



    }




    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        // TextViews
        public TextView tv_trip_date, tv_trip_max_speed, tv_trip_distance;
        public CheckBox check_trip;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();

            tv_trip_date = itemView.findViewById(R.id.tv_trip_date);
            tv_trip_max_speed = itemView.findViewById(R.id.tv_trip_max_speed);
            tv_trip_distance = itemView.findViewById(R.id.tv_trip_distance);
            check_trip = itemView.findViewById(R.id.check_trip);

            check_trip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        removeTrip(getAdapterPosition());
                    }
                    else{
                        drawTrip(getAdapterPosition());
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            //int position = getAdapterPosition();
            //drawTrip(position);
        }



        /**
         * Configure informations on infowindow when the vehicle icon is clicked
         */
        private void configureCustomInfoWindow_vehicle_stops(StopData stopData, InfoWindow custInfoWindow) {

                // TextViews
                TextView tv_stop_start, tv_stop_end, tv_stop_duration, tv_stop_counter_distance, tv_stop_counter_time, tv_stop_address;

                tv_stop_start = custInfoWindow.getView().findViewById(R.id.tv_stop_start);
                tv_stop_end = custInfoWindow.getView().findViewById(R.id.tv_stop_end);
                tv_stop_duration = custInfoWindow.getView().findViewById(R.id.tv_stop_duration);
                tv_stop_counter_distance =  custInfoWindow.getView().findViewById(R.id.tv_stop_counter_distance);
                tv_stop_counter_time = custInfoWindow.getView().findViewById(R.id.tv_stop_counter_time);
                tv_stop_address = custInfoWindow.getView().findViewById(R.id.tv_stop_address);

                String st_address = context.getString(R.string.unavailable);

                // stopData start
                String st_stop_start = (stopData.getStop_timestamp_srv_DateTime(context) != null) ? stopData.getStop_timestamp_srv_DateTime(context)  : context.getString(R.string.unavailable);
                String st_stop_end = (stopData.getNo_stop_timestamp_srv_DateTime(context) != null) ? stopData.getNo_stop_timestamp_srv_DateTime(context)  : context.getString(R.string.unavailable);
                String st_stop_duration = (stopData.getStop_duration(context) != null) ? stopData.getStop_duration(context)  : context.getString(R.string.unavailable);
                String st_stop_counter_distance = (stopData.getDistance_counter() != null && stopData.getToday_distance_counter()!= null) ? stopData.getToday_distance_counter() + " / " + stopData.getDistance_counter() + " Km"  : context.getString(R.string.unavailable);
                String st_stop_counter_time = (stopData.getTime_counter() != null && stopData.getToday_time_counter() != null) ? stopData.getToday_time_counter() + " / " + stopData.getTime_counter()  : context.getString(R.string.unavailable);

                tv_stop_start.setText(st_stop_start);
                tv_stop_end.setText(st_stop_end);
                tv_stop_duration.setText(st_stop_duration);
                tv_stop_counter_distance.setText(st_stop_counter_distance);
                tv_stop_counter_time.setText(st_stop_counter_time);

        }

    }

}
