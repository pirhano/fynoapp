package com.othmane.lamrani.fynoapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.ui.MainActivity;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class FragmentAlerts extends Fragment implements MainActivity.OnBackPressedListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alerts, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // onback pressed listener
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

    }

    @Override
    public void doBack() {
        ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_home, null);
    }
}
