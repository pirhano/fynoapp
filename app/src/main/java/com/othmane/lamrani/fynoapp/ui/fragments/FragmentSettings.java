package com.othmane.lamrani.fynoapp.ui.fragments;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.ui.MainActivity;

import java.util.Locale;

/**
 * Created by Lamrani on 31/10/2017.
 */

public class FragmentSettings extends Fragment implements MainActivity.OnBackPressedListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Inflating the fragment inside the activity
        android.app.Fragment fragment = new SettingScreen();

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        if(savedInstanceState == null){
            // created for the first the first time
            fragmentTransaction.add(R.id.screenSetting, fragment, "settings_fragment");
            fragmentTransaction.commit();
        }
        else{
            fragment = getActivity().getFragmentManager().findFragmentByTag("settings_fragment");
        }

        // onback pressed listener
        ((MainActivity)getActivity()).setOnBackPressedListener(this);
    }

    @Override
    public void doBack() {
        ((MainActivity) getActivity()).displaySelectedScreen(R.id.nav_home, null);
    }


    public static class SettingScreen extends PreferenceFragment {

        //private EditTextPreference pref_user_name ;
        private ListPreference pref_language ;
        //private SwitchPreference pref_switch1 ;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load preference screen from resource
            addPreferencesFromResource(R.xml.settings_screen);



            // configure UI
            configureview();

            // update preferences summaries when the activity is loaded
            updatePreferences();

        }

        private void configureview() {
            //pref_user_name = (EditTextPreference) findPreference("user_name");
            pref_language = (ListPreference) findPreference("language");
            //pref_switch1 = (SwitchPreference) findPreference("switch1");

            // updating username editText
            /*pref_user_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(newValue != null){
                        preference.setSummary(newValue.toString());
                    }
                    return true;
                }
            });*/

            // updating language choice
            pref_language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(newValue != null){
                        // change the locals
                        Methods.changeLocaleLanguage(getActivity(), newValue.toString());
                    }
                    return true;
                }
            });

        }

        // when the activity is loaded for the first time we update the summaries for each preference
        private void updatePreferences() {

            // Shared preference
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

            if(sharedPreferences != null){
                // get every preference
                String st_editText = sharedPreferences.getString("user_name", null);
                String language_value = sharedPreferences.getString("language", null);

                // update edit text
                //pref_user_name.setSummary(st_editText);
                // update language
                String lang_fr = getResources().getStringArray(R.array.language_list)[0];
                String lang_en = getResources().getStringArray(R.array.language_list)[1];
                String lang_ar = getResources().getStringArray(R.array.language_list)[2];
                if(language_value != null){
                    switch (language_value){
                        case "fr" : pref_language.setSummary(lang_fr);
                            break;
                        case "en" : pref_language.setSummary(lang_en);
                            break;
                        case "ar" : pref_language.setSummary(lang_ar);
                            break;
                    }
                }
            }
        }
    }

}
