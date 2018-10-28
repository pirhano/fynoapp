package com.othmane.lamrani.fynoapp.ui.fragments.cards;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.othmane.lamrani.fynoapp.API.models.VehicleSpeed;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.controller.WayPointsController;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.ui.MainActivity;
import com.othmane.lamrani.fynoapp.ui.fragments.FragmentEquipmentDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lamrani on 01/11/2017.
 */

public class CardSpeedChart extends Fragment implements MainActivity.OnBackPressedListener{

    private LineChart chart;
    private Call<VehicleSpeed> call_vehicle_speed;
    private WayPointsController wayPointsController;
    private int valueFormattedDate = 1;
    private float previous_ts;
    private RelativeLayout layout_progress;
    private Calendar myCalendar;
    private LinearLayout btn_choose_day;
    private TextView tv_stop_date;
    private DatePickerDialog.OnDateSetListener date;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.card_vehicle_speed_stats, container, false);

        return rootView;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(call_vehicle_speed != null){
            if(call_vehicle_speed.isExecuted()){
                call_vehicle_speed.cancel();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previous_ts = 0;

        // config ui
        configView();

        wayPointsController = new WayPointsController();

        // when the user clicks on the button to chose a date in order to load stops
        btn_choose_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                int month = monthOfYear + 1;

                String date = dayOfMonth + "_" + month + "_" + year;
                update_label_stop_date(dayOfMonth + "/" + month + "/" + year);

                refreshCard(date);
            }

        };

        refreshCard(null);

    }

    /**
     * Update stop date label
     */
    public void update_label_stop_date(String date){
        if(date != null){
            tv_stop_date.setText(date);
        }
    }

    private void configView() {
       if(getView() != null){
          chart = getView().findViewById(R.id.chart);

           // enable value highlighting
           chart.setHighlightPerDragEnabled(false);

           // get the legend (only possible after setting data)
           Legend l = chart.getLegend();

           // modify the legend ... by default it is on the left
           l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
           l.setForm(Legend.LegendForm.LINE);

           Description description = new Description();
           description.setText(getString(R.string.speed_description));
           //description.setPosition(0,0);
           chart.setDescription(description);


           btn_choose_day = getView().findViewById(R.id.btn_choose_day);
           tv_stop_date =  getView().findViewById(R.id.tv_speed_date);

           layout_progress = getView().findViewById(R.id.progress);
       }
    }

    /**
     * Configure general informations card
     */
    public void refreshCard(String date) {

        clearChart();

        if(date == null){
            call_vehicle_speed = wayPointsController.getSpeed(FragmentEquipmentDetails.currentTrackingDynData.getTracking_item_id(), Constants.REFERENCE.TODAY);

        }
        else{
            call_vehicle_speed = wayPointsController.getSpeed(FragmentEquipmentDetails.currentTrackingDynData.getTracking_item_id(), date);
        }

        showProgress();

        call_vehicle_speed.enqueue(new Callback<VehicleSpeed>() {
            @Override
            public void onResponse(Call<VehicleSpeed> call, Response<VehicleSpeed> response) {
                Log.i("Retrofit", "success");

                VehicleSpeed vehicleSpeed = response.body();

                if(vehicleSpeed != null){
                    List<VehicleSpeed.SpeedDetails> speedData = vehicleSpeed.getSpeedDetails();
                    // update ui

                    List<Entry> entries = new ArrayList<Entry>();

                    if(speedData != null){
                        if(!speedData.isEmpty()){
                            for (VehicleSpeed.SpeedDetails data : speedData) {

                                // turn your data into Entry objects
                                //entries.add(new Entry(data.getValueX(), data.getValueY()));

                                if(insertChartDate(data.getTime()) != -1 && previous_ts != insertChartDate(data.getTime())){
                                    previous_ts = insertChartDate(data.getTime());
                                    entries.add(new Entry(previous_ts, (float)data.getSpeed()));
                                }

                            }

                            // animate y axis
                            chart.animateY(2000); ;

                            // x Axis
                            XAxis xAxis = chart.getXAxis();
                            xAxis.setValueFormatter(new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    return getChartDateGraphic(value);
                                }
                            });
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawGridLines(false);
                            xAxis.setGranularity(1f); // only intervals of 1 day
                            xAxis.setLabelCount(7);


                            // Y axis
                            YAxis yAxisleft = chart.getAxisLeft();
                            yAxisleft.setDrawGridLines(false);
                            YAxis yAxisright = chart.getAxisRight();
                            yAxisright.setDrawGridLines(false);
                            yAxisleft.setAxisMinimum(0);
                            yAxisright.setAxisMinimum(0);


                            LineDataSet dataSet = new LineDataSet(entries,  "Vitesse (Max: " + vehicleSpeed.getMax_speed() + " Km/h)"); // add entries to dataset

                            dataSet.setColor(R.color.vehicle_sub_menu);
                            dataSet.setValueTextColor(R.color.colorPrimary); // styling, ...
                            dataSet.setDrawValues(false);

                            // fill color
                            dataSet.setDrawFilled(true);
                            dataSet.setHighLightColor(Color.RED);

                            LineData lineData = new LineData(dataSet);
                            chart.setData(lineData);
                            chart.invalidate(); // refresh
                        }
                        else{
                            clearChart();
                        }
                    }
                }
                else{
                    Toast.makeText(getContext(), R.string.intern_error, Toast.LENGTH_SHORT).show();
                }

                hideProgress();
            }

            @Override
            public void onFailure(Call<VehicleSpeed> call, Throwable t) {
                Log.i("Retrofit", "failure");
                if(!call_vehicle_speed.isCanceled()){
                    hideProgress();
                    Toast.makeText(getContext(), R.string.intern_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Show progress bar
     */
    public void showProgress(){
        layout_progress.setVisibility(View.VISIBLE);
        btn_choose_day.setEnabled(false);
    }

    /**
     * Hide progresss
     */
    public void hideProgress(){
        layout_progress.setVisibility(View.GONE);
        btn_choose_day.setEnabled(true);
    }

    private void clearChart() {
        chart.clear();
        chart.invalidate();
    }


    // *********** Helper methods for charts ****************


    // get formatted date as the pattern dd/MM/yyyy
    public String getChartDate(float timeStamp){
        Date date = new Date((long) (new Float(timeStamp).longValue()*valueFormattedDate));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    // get formatted date as the pattern dd/MM
    public String getChartDateGraphic(float timeStamp){
        Date date = new Date(new Float(timeStamp).longValue()*valueFormattedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public float insertChartDate(String date_string){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = null; //Calendar.getInstance().getTime();
        try {
            date = formatter.parse(date_string);

            return date.getTime()/valueFormattedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }


    @Override
    public void doBack() {

    }
}
