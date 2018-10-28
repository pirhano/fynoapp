package com.othmane.lamrani.fynoapp.ui.fragments.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.EquipementData;
import com.othmane.lamrani.fynoapp.API.models.Equipment;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.adapter.EquipmentAdapter;
import com.othmane.lamrani.fynoapp.controller.TrackingItemsController;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.ui.MainActivity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class TabEquipmentList extends Fragment {

    private RecyclerView mRecyclerView;
    private TrackingItemsController trackingItemsController;
    private EquipmentAdapter equipmentAdapter;
    private AppCompatActivity activity;
    public ProgressBar progressBar;
    private View dark_bg;
    private TextView tv_message;
    private ImageButton btn_refresh;
    private Call<Equipment> equipmentCall;
    private SearchView searchView;
    private List<EquipementData> equipementData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_equipment_list, container, false);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(equipmentCall != null){
            if(equipmentCall.isExecuted()){
                equipmentCall.cancel();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get activity
        activity = (AppCompatActivity)getActivity();

        // set the title
        activity.setTitle(getString(R.string.home_equipment));

        // configure the views
        configViews();

        // Configure search view
        configSearchView();

        // configure recycler view
        configureRecycleView();

        // configuring retrifut service
        configRetrofitService();

        if(MainActivity.vehicleList != null){
            if(!MainActivity.vehicleList.isEmpty()){
                hideMessage();
            }
        }

        // if the list view has already been visualised we are going to display the old values then we refresh the listview
        if(MainActivity.vehicleList != null){

            for (EquipementData item: MainActivity.vehicleList) {
                equipmentAdapter.addEquipment(item);
            }

            refreshListView();
        }
        else{
            // refreshing the list view
            refreshListView();
        }


        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // refresh the listView
                refreshListView();
            }
        });



    }

    private void configSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


    }

    /**
     * Filter the listView
     */
    public void filterList(String s){
        // remove all items from list
        equipmentAdapter.removeAllItems();

        boolean empty_result = true;
        // add items that match the string
        if(equipementData != null){

            for (EquipementData equipment: equipementData) {

                boolean add_equipment = false;

                String st_licence_plate = equipment.getTracking_dyn_data().getLicence_plate();
                String st_label = equipment.getTracking_dyn_data().getLabel();
                String st_driver = equipment.getTracking_dyn_data().getDriver();

                // verify for licence plates
                if(st_licence_plate != null){
                    if(st_licence_plate.toLowerCase().contains(s.toLowerCase())){
                        equipmentAdapter.addEquipment(equipment);
                        add_equipment = true;
                        empty_result = false;
                    }
                }
                // verify for sm type label
                if(st_label != null && !add_equipment){
                    if(st_label.toLowerCase().contains(s.toLowerCase())){
                        equipmentAdapter.addEquipment(equipment);
                        add_equipment = true;
                        empty_result = false;
                    }
                }
                // verify for driver name
                if(st_driver != null && !add_equipment){
                    if(st_driver.toLowerCase().contains(s.toLowerCase())){
                        equipmentAdapter.addEquipment(equipment);
                        empty_result = false;
                    }
                }
            }
            if(empty_result){
                showMessage(R.string.empty_equipment_list);
            }
            else{
                hideMessage();
            }
        }
    }

    /**
     * Configure the views of the fragment
    */
    private void configViews() {
        if(getView() != null){
            mRecyclerView = (RecyclerView) getView().findViewById(R.id.equipmentRecyclerView);
            progressBar = (ProgressBar) getView().findViewById(R.id.progressbar);
            dark_bg = (View) getView().findViewById(R.id.bg_dark);
            tv_message = (TextView) getView().findViewById(R.id.tv_message);
            btn_refresh = MainActivity.toolbar.findViewById(R.id.btn_refresh);
            searchView =  MainActivity.toolbar.findViewById(R.id.searchView);
        }
    }

    private void configureRecycleView() {
        // To make scrolling smoothly
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        equipmentAdapter = new EquipmentAdapter((MainActivity) getActivity());

        mRecyclerView.setAdapter(equipmentAdapter);
    }

    public void configRetrofitService(){

        trackingItemsController = new TrackingItemsController();
    }

    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        dark_bg.setVisibility(View.VISIBLE);
        EquipmentAdapter.locked = true;
        if(TabEquipmentMap.progressBar != null){
            TabEquipmentMap.progressBar.setVisibility(View.VISIBLE);
            TabEquipmentMap.dark_bg.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        dark_bg.setVisibility(View.GONE);
        EquipmentAdapter.locked = false;
        if(TabEquipmentMap.progressBar != null){
            TabEquipmentMap.progressBar.setVisibility(View.GONE);
            TabEquipmentMap.dark_bg.setVisibility(View.GONE);
        }
    }

    public void showMessage(int id){

        int id_drawable = 0;
        switch(id){
            case R.string.loading_data:{
                id_drawable = R.drawable.transparent;
                break;
            }
            case R.string.network_disabled:{
                id_drawable = R.drawable.ic_network_wireless_disconnected;
                break;
            }
            case R.string.intern_error:{
                id_drawable = R.drawable.ic_error_large;
                break;
            }
            case R.string.network_weak:{
                id_drawable = R.drawable.ic_network_wireless_disconnected;
                break;
            }
            case R.string.empty_equipment_list:{
                id_drawable = R.drawable.ic_no_results;
                break;
            }
        }

        tv_message.setCompoundDrawablesRelativeWithIntrinsicBounds(0, id_drawable, 0, 0);

        tv_message.setVisibility(View.VISIBLE);
        if(getActivity() != null && isAdded()){
            tv_message.setText(getString(id));
        }

    }

    public void hideMessage(){
        tv_message.setVisibility(View.GONE);
    }


    public void refreshListView(){

        if(Methods.isNetworkAvailable(getContext())){
            // show progress bar and informative message
            showProgressBar();
            if(MainActivity.vehicleList != null){
                if(MainActivity.vehicleList.isEmpty()){
                    showMessage(R.string.loading_data);
                }
            }
            else{
                showMessage(R.string.loading_data);
            }

            // snackbar showing loading data
            final Snackbar snackbar = Snackbar.make(getView(), getString(R.string.loading), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            if( Methods.getUser() != null){

                int user_id = Methods.getUser().getId();

                equipmentCall = trackingItemsController.getAllTrackingItems(user_id);

                equipmentCall.enqueue(new Callback<Equipment>() {
                    @Override
                    public void onResponse(Call<Equipment> call, Response<Equipment> response) {
                        Log.i("Retrofit", "success");

                        Equipment equipment_response = response.body();

                        if(equipment_response != null){

                            equipementData = equipment_response.getData();

                            // if the list is empty we are going to hide the progress bar and display a message
                            if(equipementData.isEmpty()){
                                showMessage(R.string.empty_equipment_list);
                            }
                            else{

                                hideMessage();

                                if(MainActivity.vehicleList != null){
                                    if(!MainActivity.vehicleList.isEmpty()){
                                        // clear list
                                        MainActivity.vehicleList.clear();
                                        // remove items from recycler view
                                        equipmentAdapter.removeAllItems();
                                    }
                                }
                                else{
                                    MainActivity.vehicleList = new ArrayList<>();
                                }

                                // remove all tracking items
                                equipmentAdapter.removeAllItems();

                                int engine_on = 0;

                                for (EquipementData equipment : equipementData) {

                                    if(equipment.getTracking_dyn_data().getLast_engine_state() == 1){
                                        engine_on ++;
                                    }

                                    // add equipment to the global variable
                                    MainActivity.vehicleList.add(equipment);

                                    // add equipment to the adapter
                                    equipmentAdapter.addEquipment(equipment);
                                }

                                TabLayout tabLayout = activity.findViewById(R.id.tabs);
                                TabLayout.Tab tab = tabLayout.getTabAt(0);
                                tab.setText(tab.getText() + " (" + engine_on + "/" + equipementData.size() + ")");
                                //activity.getSupportActionBar().setTitle(activity.getSupportActionBar().getTitle() + " (" + engine_on + "/" + equipementData.size() + ")");
                            }
                        }
                        else{
                            // remove all tracking items
                            equipmentAdapter.removeAllItems();
                            showMessage(R.string.intern_error);
                        }

                        // hiding progress bar
                        hideProgressBar();

                        TabEquipmentMap.refreshVehiclesPosition(getContext());

                        snackbar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Equipment> call, Throwable t) {
                        Log.i("Failure", "retrofit");

                        if(!call.isCanceled()){
                            // remove all tracking items
                            equipmentAdapter.removeAllItems();

                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                            showMessage(R.string.intern_error);
                        }
                        else {
                            //Toast.makeText(getContext(), "Loading data cancelled", Toast.LENGTH_SHORT).show();
                            Log.i("Retrofit", "Cancelled");
                        }

                        // hide progress bar and show error message
                        hideProgressBar();
                        // If the connection timeout has occured
                        if(t instanceof SocketTimeoutException){
                            showMessage(R.string.network_weak);
                        }

                        snackbar.dismiss();
                    }
                });
            }
            else{
                showMessage(R.string.intern_error);
            }






        }
        else{
            equipmentAdapter.removeAllItems();
            showMessage(R.string.network_disabled);
            hideProgressBar();
            Methods.snackbarNetworkUnavailable(getContext(), getView(), null);
        }

    }


}
