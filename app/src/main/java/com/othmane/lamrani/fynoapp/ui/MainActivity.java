package com.othmane.lamrani.fynoapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.API.models.EquipementData;
import com.othmane.lamrani.fynoapp.API.models.UserDetails;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Methods;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentAlerts;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentContactUs;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipment;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipmentDetails;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentHome;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentProfile;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentHistory;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentSettings;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment fragment = null;
    //public static TrackingDynData selectedEquipment;
    public static List<EquipementData> vehicleList;
    private SearchView searchView;
    private ImageButton btn_refresh;
    public TextView tv_name;
    public TextView tv_service;
    public static Toolbar toolbar;
    protected OnBackPressedListener onBackPressedListener;
    public static UserDetails userDetails;
    public static DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set the context value
        if(Methods.context == null){
            Methods.context= getApplicationContext();
        }

        // searsh view and btn_refresh
        searchView = (SearchView) findViewById(R.id.searchView);
        btn_refresh = (ImageButton) findViewById(R.id.btn_refresh);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // user informations
        View header = navigationView.getHeaderView(0);
        tv_name = (TextView) header.findViewById(R.id.tv_user_name);
        tv_service = (TextView) header.findViewById(R.id.tv_user_service);

        // display home fragment
        displaySelectedScreen(R.id.nav_home, null);



        // set listener on the fragment instance in order to show / hide some components in action bar
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(fragment instanceof FragmentHome){
                    Log.d("Selected fragment", "Fragment Home");
                    // set the title
                    setTitle("");
                    // hide action bar
                    hideActionbar();
                }
                else if(fragment instanceof FragmentEquipment){
                    Log.d("Selected fragment", "Fragment Equipment");
                    // set the title
                    setTitle(getString(R.string.home_equipment));
                    // show action bar
                    showActionbar();
                    // show refresh btn
                    showRefreshButton();
                    // show searchview
                    showSearchView();
                }
                else if(fragment instanceof FragmentEquipmentDetails){
                    Log.d("Selected fragment", "Fragment Equipment details");
                    // hide action bar
                    hideActionbar();
                    // show refresh btn
                    showRefreshButton();
                    // hide searchview
                    hideSearchView();
                }
                else if(fragment instanceof FragmentSettings){
                    Log.d("Selected fragment", "Fragment Settings");
                    // set the title
                    setTitle(getString(R.string.home_settings));
                    // show action bar
                    showActionbar();
                    // hide refresh btn
                    hideSearchView();
                    // hide searchview
                    hideRefreshButton();
                }
                else if(fragment instanceof FragmentProfile){
                    Log.d("Selected fragment", "Fragment Profile");
                    // set the title
                    //setTitle(getString(R.string.profile));
                    // show action bar
                    hideActionbar();
                    // hide refresh btn
                    hideRefreshButton();
                    // hide searchview
                    hideSearchView();
                }
                else if(fragment instanceof FragmentAlerts){
                    Log.d("Selected fragment", "Fragment Alerts");
                    // set the title
                    setTitle(getString(R.string.home_alerts));
                    // show action bar
                    showActionbar();
                    // show refresh btn
                    showRefreshButton();
                    // hide searchview
                    hideSearchView();
                }
                else if(fragment instanceof FragmentHistory){
                    Log.d("Selected fragment", "Fragment Reporting");
                    // set the title
                    //setTitle(getString(R.string.home_history));
                    // show action bar
                    showActionbar();
                    // show refresh btn
                    showRefreshButton();
                    // hide searchview
                    hideSearchView();
                }
            }
        });


    }


    /**
     * Show action bar
     */
    public void showActionbar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().show();
        }
    }

    /**
     * Hides action bar
     */
    public void hideActionbar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    /**
     * Shows the search view in the action bar
     */
    public void showSearchView(){
        searchView.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the search view from action bar
     */
    public void hideSearchView(){
        searchView.setVisibility(View.GONE);
    }

    /**
     * Shows the refresh button
     */
    public void showRefreshButton(){
        btn_refresh.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the refresh button
     */
    public void hideRefreshButton(){
        btn_refresh.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
           hideMenu();
        } else {

            // on back listener do back
            if(onBackPressedListener != null){
                onBackPressedListener.doBack();
            }
        }


    }

    // ***************** On back pressed listener *******************

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }



    // **************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id, null);

        hideMenu();
        return true;
    }


    /**
     * Displays the selected fragment that match the given id
     * @param id id of the fragment
     */
    public void displaySelectedScreen(int id, Bundle bundle){

        boolean isFragment = true;

        switch (id) {

            case R.id.nav_home: {
                fragment = new FragmentHome();
                break;
            }
            case R.id.nav_equipments: {
                if(fragment != null){
                    for(int i = 0; i <  getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
                fragment = new FragmentEquipment();
                break;
            }
            case R.id.nav_equipment_details: {

                fragment = new FragmentEquipmentDetails();
                break;
            }
            case R.id.nav_alerts: {
                fragment = new FragmentAlerts();
                break;
            }
            case R.id.nav_history: {
                if(fragment != null){
                    for(int i = 0; i <  getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
                fragment = new FragmentHistory();
                if(bundle == null){
                    fragment = new FragmentEquipment();
                }
                break;
            }
            case R.id.nav_settings: {
                fragment = new FragmentSettings();
                break;
            }
            case R.id.nav_profile: {
                fragment = new FragmentProfile();
                break;
            }
           /* case R.id.nav_contact: {
                fragment = new FragmentContactUs();
                break;
            }*/
            case R.id.nav_logout: {
                isFragment = false;
                Runnable logout = new Runnable() {
                    @Override
                    public void run() {
                        // remove the current user from shared preferences
                        //userController.removeLoggedUserFromSP();

                        // Leave the current activity and move to the login_activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        // destroy current user token and informations
                        Methods.destroyUser();
                        // close the current activity
                        finish();
                    }
                };
                showConfirmationLogout(logout);

                break;
            }
            default:{
                Toast.makeText(getApplicationContext(), "Action not available", Toast.LENGTH_SHORT).show();
            }
        }

        if (fragment != null && isFragment) {

            /*if(getSupportFragmentManager().popBackStackImmediate()){
                Log.d("Replacing fragment", "A fragment is popped");
            }*/

            // pass the params
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();

            List<Fragment> fragments = getSupportFragmentManager().getFragments();

            if (fragments != null) {
                Log.i("Fragments list", fragments.size() + "");
                for (Fragment fragment : fragments) {
                    // Warning: one fragment is null and we should be aware of that
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        Log.i("Fragment", fragment.toString());
                    }
                }
            }

            fragmentTransaction.show(fragment);

            // Replace the fragment
            showFragment(fragment);

        }

        hideMenu();
    }

    /**
     * Show fragment
     */
    public void showFragment(Fragment fragment_to_show){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.screens, fragment_to_show, fragment_to_show.getClass().getName());
        fragmentTransaction.addToBackStack(MainActivity.class.toString());
        fragmentTransaction.commit();
    }

    public  void hideMenu(){
        drawer.closeDrawer(GravityCompat.START);
    }

    public static void showMenu(){
        drawer.openDrawer(GravityCompat.START);
    }

    /**
     * Show confirmation to logout
     * @param logout
     */
    public void showConfirmationLogout(final Runnable logout){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.confirm_logout))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout.run();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
