package com.othmane.lamrani.fynoapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.TempUserInfo;
import com.othmane.lamrani.fynoapp.API.models.UserDetails;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.API.models.User;
import com.othmane.lamrani.fynoapp.API.models.VehicleInformations;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentHome;

import org.apache.commons.lang3.StringUtils;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lamrani on 25/09/2017.
 */

public class Methods {

    public static Context context;

    /**
     * Save the connected user
     * @param user
     */
    public static void saveUser(Context context, TempUserInfo user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.REFERENCE.USER, MODE_PRIVATE);

        if(sharedPreferences != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.REFERENCE.USER_ID, user.getId());
            editor.putString(Constants.REFERENCE.USER_NAME, user.getName());
            editor.putString(Constants.REFERENCE.USER_SERVICE, user.getJob_function());
            editor.putBoolean(Constants.REFERENCE.USER_REMEMBER, user.getRemember_me());
            editor.putString(Constants.REFERENCE.USER_TOKEN, user.getToken());
            editor.apply();
        }
    }

    /**
     * Get
     * @return the connected user from shared preferences
     */
    public static TempUserInfo getUser(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.REFERENCE.USER, MODE_PRIVATE);

        if(sharedPreferences != null){
            Integer id = sharedPreferences.getInt(Constants.REFERENCE.USER_ID, 0);
            String name = sharedPreferences.getString(Constants.REFERENCE.USER_NAME, "Name " + context.getString(R.string.unavailable));
            String service = sharedPreferences.getString(Constants.REFERENCE.USER_SERVICE, "Service "  + context.getString(R.string.unavailable));
            String token = sharedPreferences.getString(Constants.REFERENCE.USER_TOKEN, null);
            boolean remember_me = sharedPreferences.getBoolean(Constants.REFERENCE.USER_REMEMBER, false);

            return  new TempUserInfo(id, name, service, token, remember_me);
        }

        return null;
    }


    /**
     * Destroy the user when logout
     */
    public static void destroyUser(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.REFERENCE.USER, MODE_PRIVATE);

        if(sharedPreferences != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }


    /**
     * Format a date
     */
    public static String getDateTime(Context context, String date_string){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = null; //Calendar.getInstance().getTime();
        try {
            date = formatter.parse(date_string);

            formatter = new SimpleDateFormat("EEEE dd/MM/yyyy, HH:mm", Locale.getDefault());

            return StringUtils.capitalize(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return context.getString(R.string.unavailable);
    }

    /**
     * Format a date
     */
    public static String getTimeOnly(Context context, String date_string){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = null; //Calendar.getInstance().getTime();
        try {
            date = formatter.parse(date_string);

            formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return context.getString(R.string.unavailable);
    }

    /**
     * Tell you if the date is today or not
     */
    public static boolean isToday(String date_string){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = null; //Calendar.getInstance().getTime();

        // Yesterday value
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday
        Date today = c1.getTime();

        try {
            date = formatter.parse(date_string);

            if(date != null){
                if(date.before(today)){
                    return false;
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return true;
    }

    /**
     * Format a date
     */
    public static String getDateTypeForChart(Context context, String date_string){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = null; //Calendar.getInstance().getTime();
        try {
            date = formatter.parse(date_string);

            formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return context.getString(R.string.unavailable);
    }

    /**
     * set local langauage
     */
    public static void setLocale(Context context){
        // Shared preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language_value = sharedPreferences.getString("language", "fr");
        Locale locale = new Locale(language_value);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.locale= locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /**
     * Set current language
     */
    // Change locale application language
    public static void changeLocaleLanguage(Activity activity, String lang){
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        // refrech the activity or fragment

        //this.finish();

        Intent refresh = new Intent(activity, MainActivity.class); //.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(refresh);
        activity.finish();

        ((MainActivity)activity).displaySelectedScreen(R.id.nav_settings, null);

        // update boolean value in previous activity to refresh it when back is pressed
        FragmentHome.languageChanged = true;
    }

    /**
     * Check if the network is available
     * @return boolean
     */
    public static  boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};

        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Make the device vibrate
     */
    public static void vibrateDevice(Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if(v != null){
            v.vibrate(500);
        }
    }

    /**
     * Convert lat & lon to readable address
     *
     */
    public static String getAddress(Context context, GeoPoint point){

        String ret = "ERROR";

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address>addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            if (addresses.isEmpty()) {
                ret = "No Address returned!";
            } else {
                ret = addresses.get(0).getCountryName() + ""+ addresses.get(0).getFeatureName();
            }
        } catch (IOException e) {
            ret="We don't know your address...";
            e.printStackTrace();
        }
        //Toast.makeText(context, ret, Toast.LENGTH_SHORT).show();

        return ret;

    }

    /**
     * display snack bar when the network is unavailable
     */
    public static void snackbarNetworkUnavailable(final Context context, View view, String message){
        if(context != null && view != null){
            if(message == null){
                message = context.getString(R.string.network_disabled);
            }

            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    /*.setAction( context.getString(R.string.connect_network), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(context, "Connecting", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                            context.startActivity(intent);
                        }
                    })*/
                    .setActionTextColor(context.getResources().getColor(android.R.color.holo_green_light ));
            snackbar.show();
        }
    }


}
