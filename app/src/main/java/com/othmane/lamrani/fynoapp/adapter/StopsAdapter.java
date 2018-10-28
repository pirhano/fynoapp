package com.othmane.lamrani.fynoapp.adapter;


import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.othmane.lamrani.fynoapp.API.models.StopData;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipmentDetails;

import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lamrani on 21/09/2017.
 */

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.Holder>{

    private List<StopData> mStopData;
    private MainActivity activity;

    // use equipement instead of tracking dyn data

    public StopsAdapter(MainActivity activity){
        mStopData = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        StopData stopData = mStopData.get(position);

        String st_address = activity.getString(R.string.unavailable);

        // stopData start
        String st_stop_start = (stopData.getStop_timestamp_srv_TimeOnly(activity) != null) ? stopData.getStop_timestamp_srv_TimeOnly(activity) + " - " + stopData.getNo_stop_timestamp_srv_TimeOnly(activity)  : activity.getString(R.string.unavailable);
        //String st_stop_end = (stopData.getNo_stop_timestamp_srv(activity) != null) ? stopData.getNo_stop_timestamp_srv(activity)  : activity.getString(R.string.unavailable);
        String st_stop_duration = (stopData.getStop_duration(activity) != null) ? stopData.getStop_duration(activity)  : activity.getString(R.string.unavailable);
        //String st_stop_counter_distance = (stopData.getDistance_counter() != null && stopData.getToday_distance_counter()!= null) ? stopData.getToday_distance_counter() + " / " +stopData.getDistance_counter() + " Km"  : activity.getString(R.string.unavailable);
        //String st_stop_counter_time = (stopData.getTime_counter() != null && stopData.getToday_time_counter() != null) ? stopData.getToday_time_counter() + " / " + stopData.getTime_counter()  : activity.getString(R.string.unavailable);

        holder.tv_stop_start.setText(st_stop_start);
        //holder.tv_stop_end.setText(st_stop_end);
        holder.tv_stop_duration.setText(st_stop_duration);
        //holder.tv_stop_counter_distance.setText(st_stop_counter_distance);
        //holder.tv_stop_counter_time.setText(st_stop_counter_time);
        holder.tv_stop_address.setText(st_address);

    }

    @Override
    public int getItemCount() {
        return mStopData.size();
    }

    public void addStop(StopData stopData) {
        mStopData.add(stopData);
        notifyDataSetChanged();
    }

    public void removeAllItems(){
        mStopData.clear();
        notifyDataSetChanged();
    }

    /**
     *
     * @param position
     * @return
     */
    public StopData getSelectedStop(int position) {
        return mStopData.get(position);
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        // TextViews
        public TextView tv_stop_start, tv_stop_end, tv_stop_duration, tv_stop_counter_distance, tv_stop_counter_time, tv_stop_address;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();


            tv_stop_start = itemView.findViewById(R.id.tv_stop_start);
            tv_stop_end = itemView.findViewById(R.id.tv_stop_end);
            tv_stop_duration = itemView.findViewById(R.id.tv_stop_duration);
            tv_stop_counter_distance =  itemView.findViewById(R.id.tv_stop_counter_distance);
            tv_stop_counter_time = itemView.findViewById(R.id.tv_stop_counter_time);
            tv_stop_address = itemView.findViewById(R.id.tv_stop_address);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            showDetails(position);
        }

        /**
         *
         * @param position: position of the current selceted item
         */
        public void showDetails(int position){

            final StopData selectedStopData = mStopData.get(position);

            FragmentEquipmentDetails.stop_marker = new Marker(FragmentEquipmentDetails.map);
            FragmentEquipmentDetails.stop_marker.setPosition(selectedStopData.getGeopoint());
            FragmentEquipmentDetails.stop_marker.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_parking_color_24));
            InfoWindow custInfoWindow_stops = new InfoWindow(R.layout.custom_info_window_onclick_stops, FragmentEquipmentDetails.map) {
                @Override
                public void onOpen(Object item) {
                    InfoWindow.closeAllInfoWindowsOn(FragmentEquipmentDetails.map);
                }

                @Override
                public void onClose() {
                    InfoWindow.closeAllInfoWindowsOn(FragmentEquipmentDetails.map);
                }
            };

            // configure the customized infowindow
            configureCustomInfoWindow_vehicle_stops(selectedStopData, custInfoWindow_stops);

            // set the customized infowindow
            FragmentEquipmentDetails.stop_marker.setInfoWindow(custInfoWindow_stops);

            // add the marker
            FragmentEquipmentDetails.map.getOverlays().add(FragmentEquipmentDetails.stop_marker);
            FragmentEquipmentDetails.map.invalidate();

            // hide stops card
            FragmentEquipmentDetails.hideCard(FragmentEquipmentDetails.current_fragment);

            // move the camera to the right position of the stop
            FragmentEquipmentDetails.stop_marker.showInfoWindow();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentEquipmentDetails.map.getController().animateTo(selectedStopData.getGeopoint());
                }
            }, 300);

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
