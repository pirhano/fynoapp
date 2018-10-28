package com.othmane.lamrani.fynoapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.adapter.EquipmentAdapter;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.Tabs.TabEquipmentList;
import com.othmane.lamrani.fynoapp.ui.fragments.Tabs.TabEquipmentMap;

/**
 * Created by Lamrani on 31/10/2017.
 */

public class FragmentEquipment extends Fragment implements MainActivity.OnBackPressedListener{


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment,container, false);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // onback pressed listener
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        return view;
    }

    public  void showEquipmentTab(){
        if(tabs != null){
            TabLayout.Tab tab_equipment_list = tabs.getTabAt(0);
            if (tab_equipment_list != null){
                tab_equipment_list.select();
            }
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        // attention --> use getChildFragmentManager instead of get fragment manager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void doBack() {

        if (tabs != null){
            if(tabs.getSelectedTabPosition() == 1){
                showEquipmentTab();
            }
            else{
                if(EquipmentAdapter.item_expanded != null){
                    if(EquipmentAdapter.item_expanded && EquipmentAdapter.expanded_view != null){
                        // close the item that is already expanded
                        EquipmentAdapter.expanded_view.setVisibility(View.GONE);
                        EquipmentAdapter.item_expanded = false;
                    }
                    else{
                        ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_home, null);
                    }
                }
                else{
                    ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_home, null);
                }

            }
        }
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
                    return new TabEquipmentList();
                case 1:
                    return new TabEquipmentMap();

                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        // returning the tab title (General, Images, History)
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab1_equipment);
                case 1:
                    return getResources().getString(R.string.tab2_equipment);
            }
            return null;
        }
    }


}
