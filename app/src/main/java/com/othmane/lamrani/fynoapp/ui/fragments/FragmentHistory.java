package com.othmane.lamrani.fynoapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.othmane.lamrani.fynoapp.API.models.TrackingDynData;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.Tabs.TabHistoryAlerts;
import com.othmane.lamrani.fynoapp.ui.fragments.Tabs.TabHistoryItinerary;
import com.othmane.lamrani.fynoapp.ui.fragments.Tabs.TabHistoryStops;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class FragmentHistory extends Fragment implements MainActivity.OnBackPressedListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabs;
    public static boolean back_to_details = false;
    public static TrackingDynData currentTrackingDynData;
    public static String licence_plates;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vehicle_history,container, false);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // onback pressed listener
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        // getting the equipment passed
        Bundle bundle = this.getArguments();
        if(bundle != null){
            back_to_details = bundle.getBoolean(Constants.REFERENCE.EQUIPMENT_back_to_details);
            currentTrackingDynData = (TrackingDynData) bundle.getSerializable(Constants.REFERENCE.EQUIPMENT);
            licence_plates = bundle.getString(Constants.REFERENCE.EQUIPMENT_licence_plate);

            if(licence_plates != null){
                getActivity().setTitle(getString(R.string.home_history) + " (" + licence_plates + ")");
            }

        }
        else{
            Snackbar.make(getView(), getString(R.string.intern_error), Snackbar.LENGTH_INDEFINITE).show();
        }


        return view;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        back_to_details = false;
        currentTrackingDynData = null;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        // attention --> use getChildFragmentManager instead of get fragment manager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new TabHistoryStops();
                case 1:
                    return new TabHistoryItinerary();
                case 2:
                    return new TabHistoryAlerts();

                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        // returning the tab title (General, Images, History)
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab1_history);
                case 1:
                    return getResources().getString(R.string.tab2_history);
                case 2:
                    return getResources().getString(R.string.tab3_history);
            }
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void doBack() {

        // if the call is alrealy executing we are going to cancel it
        if(TabHistoryItinerary.itineraries_call != null){
            if(TabHistoryItinerary.itineraries_call.isExecuted()){
                TabHistoryItinerary.itineraries_call.cancel();
            }
        }
        // if the call is alrealy executing we are going to cancel it
        if(TabHistoryStops.stops_call != null){
            if(TabHistoryStops.stops_call.isExecuted()){
                TabHistoryStops.stops_call.cancel();
            }
        }

        if(back_to_details){

            Bundle b = new Bundle();
            b.putSerializable(Constants.REFERENCE.EQUIPMENT, currentTrackingDynData);

            ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_equipment_details, b);
        }
        else{
            ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_equipments, null);
        }
    }
}
