package com.othmane.lamrani.fynoapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.othmane.lamrani.fynoapp.API.models.EquipementData;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lamrani on 21/09/2017.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.Holder> {

    private List<EquipementData> mTrackingData;
    private MainActivity activity;
    public static Boolean item_expanded;
    public static View expanded_view;
    public static boolean locked = false;

    // use equipement instead of tracking dyn data

    public EquipmentAdapter (MainActivity activity){
        mTrackingData = new ArrayList<>();
        this.activity = activity;
        item_expanded = false;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        final EquipementData equipment = mTrackingData.get(position);

        String st_vehicle_id = (equipment.getTracking_dyn_data().getLicence_plate() != null) ?  equipment.getTracking_dyn_data().getLicence_plate() : activity.getString(R.string.unavailable);
        String st_vehicle_type = (equipment.getTracking_dyn_data().getLabel() != null) ? "(" + equipment.getTracking_dyn_data().getLabel() + ")" : "";
        String st_driving_time = "--";
        String st_driving_distance = "--";
        String st_last_signal = (equipment.getTracking_dyn_data().getLast_timestamp_cs_DateTime(activity) != null) ? equipment.getTracking_dyn_data().getLast_timestamp_cs_TimeOnly(activity) : "";;

        /*if(!equipment.getTracking_dyn_data().isTodaySignal()){
            holder.tv_last_signal.setTextColor(Color.RED);
            holder.tv_vehicle_id.setTextColor(Color.RED);
        }*/

        holder.tv_vehicle_id.setText(st_vehicle_id);
        holder.tv_vehicle_type.setText(st_vehicle_type);
        holder.tv_driving_time.setText(st_driving_time);
        holder.tv_distance.setText(st_driving_distance);
        holder.tv_last_signal.setText(st_last_signal);

        int id_stop_state = R.drawable.ic_is_stopped_off;
        if(equipment.getTracking_dyn_data().getLast_stop_state() == 1) {
            id_stop_state = R.drawable.ic_is_stopped ;
        }
        holder.iv_engine_state.setImageResource(id_stop_state);

        int id_key_state = R.drawable.ic_key;
        if(equipment.getTracking_dyn_data().getLast_key_state() == 1){
            id_key_state = R.drawable.ic_key_on;
        }
        holder.iv_key_state.setImageResource(id_key_state);

        Integer distance_counter = equipment.getTracking_dyn_data().getLast_distance_counter();
        Integer time_counter = equipment.getTracking_dyn_data().getLast_time_counter();
        Integer today_distance_counter = equipment.getTracking_dyn_data().getLast_today_distance_counter();
        Integer today_time_counter = equipment.getTracking_dyn_data().getLast_today_time_counter();

        String st_distance_counter = (distance_counter != null) ? String.valueOf(distance_counter.intValue()) + " km" :  "--";
        String st_time_counter = (time_counter != null) ? String.valueOf( time_counter.intValue()) + " h" :  "--";
        //holder.tv_distance.setText(st_today_distance_counter  + st_distance_counter );
        //holder.tv_driving_time.setText(st_today_time_counter  + st_time_counter );
        String st_today_distance_counter = (today_distance_counter != null) ? today_distance_counter + " km / " + st_distance_counter :  "--";
        String st_today_time_counter = (today_time_counter != null) ? today_time_counter + " h / " + st_time_counter : "--";

        holder.tv_distance.setText(st_today_distance_counter );
        holder.tv_driving_time.setText(st_today_time_counter );

        String st_first_activity = (equipment.getTracking_dyn_data().getToday_first_activity_DateTime(activity) != null) ? equipment.getTracking_dyn_data().getToday_first_activity_Time(activity) : activity.getString(R.string.unavailable);
        holder.tv_vehicle_first_activity.setText(st_first_activity);

        // address
        String st_address = "--";
        holder.tv_vehicle_current_address.setText(st_address);

        // onclick listener for expanding menu
        holder.btn_expand_equipment_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locked){
                    // if the menu is already expanded
                    if(item_expanded && expanded_view != null){
                        // close the item that is already expanded
                        expanded_view.setVisibility(View.GONE);
                        item_expanded = false;
                    }
                    else{
                        holder.sub_item_container.setVisibility(View.VISIBLE);
                        expanded_view = holder.sub_item_container;
                        item_expanded = true;
                    }

                    // if the user clicked on another item
                    if(expanded_view != holder.sub_item_container){
                        holder.sub_item_container.setVisibility(View.VISIBLE);
                        expanded_view = holder.sub_item_container;
                        item_expanded = true;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrackingData.size();
    }

    public void addEquipment(EquipementData equipment) {
        mTrackingData.add(equipment);
        notifyDataSetChanged();
    }

    public void removeAllItems(){
        mTrackingData.clear();
        notifyDataSetChanged();
    }

    /**
     *
     * @param position
     * @return
     */
    private EquipementData getSelectedEquipment(int position) {
        return mTrackingData.get(position);
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
         TextView tv_vehicle_id;
         TextView tv_vehicle_type;
         TextView tv_last_signal;
         ImageView iv_engine_state;
         ImageView iv_key_state;
         TextView tv_distance;
         TextView tv_driving_time;
         TextView tv_vehicle_first_activity;
         TextView tv_vehicle_current_address;
         LinearLayout btn_expand_equipment_item, sub_item_container;
         Button btn_history;



        Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();

            tv_vehicle_id = itemView.findViewById(R.id.tv_vehicle_id);
            tv_vehicle_type = itemView.findViewById(R.id.tv_vehicle_type);
            tv_last_signal = itemView.findViewById(R.id.tv_vehicle_lastSignal);
            tv_distance = itemView.findViewById(R.id.tv_vehicle_distance);
            tv_driving_time = itemView.findViewById(R.id.tv_vehicle_driving_time);
            iv_engine_state = itemView.findViewById(R.id.iv_vehicle_stop_state);
            iv_key_state =  itemView.findViewById(R.id.iv_vehicle_engine_state);
            tv_vehicle_first_activity = itemView.findViewById(R.id.tv_vehicle_first_activity);
            tv_vehicle_current_address = itemView.findViewById(R.id.tv_vehicle_current_address);

            btn_expand_equipment_item = itemView.findViewById(R.id.btn_expand_equipment_item);
            sub_item_container = itemView.findViewById(R.id.sub_item_container);

        }

        @Override
        public void onClick(View v) {
            if(!locked){
                int position = getAdapterPosition();
                showDetails(position);
            }
        }

        /**
         *
         * @param position: position of the current selceted item
         */
        public void showDetails(int position){

            EquipementData selectedEquipment = getSelectedEquipment(position);

            //Intent intent = new Intent(context, EquipmentDetailsActivity.class);
            //intent.putExtra(Constants.REFERENCE.EQUIPMENT, selectedEquipment);
            //context.startActivity(intent);

            //activity.selectedEquipment = selectedEquipment;

            Bundle b = new Bundle();
            b.putSerializable(Constants.REFERENCE.EQUIPMENT, selectedEquipment.getTracking_dyn_data());

            // show Equipment details fragment
            activity.displaySelectedScreen(R.id.nav_equipment_details, b);

        }

    }

}
