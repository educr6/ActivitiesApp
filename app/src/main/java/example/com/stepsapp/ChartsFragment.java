package example.com.stepsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import static example.com.stepsapp.DatabaseHelper.COL1;
import static example.com.stepsapp.DatabaseHelper.COL2;
import static example.com.stepsapp.DatabaseHelper.COL3;
import static example.com.stepsapp.DatabaseHelper.COL4;
import static example.com.stepsapp.DatabaseHelper.COL5;
import static example.com.stepsapp.DatabaseHelper.COL6;
import static example.com.stepsapp.DatabaseHelper.TABLE_NAME;
import static example.com.stepsapp.MainActivity.activityLog;
import static example.com.stepsapp.MainActivity.generalDateFormat;
import static example.com.stepsapp.MainActivity.getTimeDiff;
import static java.lang.Float.parseFloat;

import java.util.ArrayList;

import static java.lang.Float.parseFloat;

public class ChartsFragment extends Fragment {

    PieChart stepschart;
    PieChart activitiesChart;
    SharedPreferences prefs;
    TextView stepsView;

    public ChartsFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        prefs = getActivity().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

        stepsView = getActivity().findViewById(R.id.chartStepsTaken);

        float steps = prefs.getFloat("steps", -1f);
        float secondsWalking = prefs.getFloat("SecondsOnTheGo", -1);

        stepsView.setText("Steps taken: " + steps);




        stepschart = getActivity().findViewById(R.id.stepschart);
        stepschart.setUsePercentValues(true);
        stepschart.getDescription().setEnabled(false);

        stepschart.setDragDecelerationFrictionCoef(0.95f);

        stepschart.setDrawHoleEnabled(true);
        stepschart.setHoleColor(Color.WHITE);
        stepschart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> values  = new ArrayList<>();

        values.add(new PieEntry(steps, "Steps taken today"));
        values.add(new PieEntry(6000f - steps, "Steps to achieve goal" ));

        PieDataSet dataSet = new PieDataSet(values, "Steps");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        stepschart.setData(data);

        activityLog = (new DatabaseHelper(getContext())).getReadableDatabase();

        String[] activityNames = DatabaseHelper.fetchActivityNames(activityLog);
        String[] durations = DatabaseHelper.fetchDuration(activityLog);

        float[] durationsFloat = new float[durations.length];

        for (int i = 0; i < durationsFloat.length; i++) {
            durationsFloat[i] = convertMinutes(durations[i]);
        }


        activitiesChart = getActivity().findViewById(R.id.activitiesChart);
        activitiesChart.setUsePercentValues(true);
        activitiesChart.getDescription().setEnabled(false);

        activitiesChart.setDragDecelerationFrictionCoef(0.95f);

        activitiesChart.setDrawHoleEnabled(true);
        activitiesChart.setHoleColor(Color.WHITE);
        activitiesChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> values2  = new ArrayList<>();

        for (int i = 0; i < durationsFloat.length; i++) {

            if (durationsFloat[i] != -1) {

                values2.add(new PieEntry(durationsFloat[i], activityNames[i]));

            }
        }



        PieDataSet dataSet2 = new PieDataSet(values2, "Activities");
        dataSet2.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data2 = new PieData(dataSet2);
        data2.setValueTextSize(10f);
        data2.setValueTextColor(Color.YELLOW);

        activitiesChart.setData(data2);




    }

    public float convertMinutes(String time) {

        if (time == null)
            return -1;

        boolean m = false;
        boolean h = false;
        float result = 0;

        for(int i = 0; i < time.length(); i++) {
            if ( time.charAt(i) == 'M' ) {
                m = true;

            }

            if (time.charAt(i) == 'H') {
                h = true;
            }

        }

        float hours = 0;
        float minutes = 0;
        float seconds = 0;
        int indexMarker = 0;

        if(h) {
            String hoursValue = "";
            for (int i = 0; time.charAt(i) != 'H' ; i++) {
                hoursValue += time.charAt(i);
                indexMarker = i;
            }
            indexMarker += 3;
            hours = parseFloat(hoursValue);
        }

        if (m) {
            String minutesValue = "";
            for (int i = indexMarker; time.charAt(i) != 'M'; i++) {
                minutesValue += time.charAt(i);
                indexMarker = i;
            }

            indexMarker += 3;
            minutes = parseFloat(minutesValue);
        }

        String secondsValue = "";
        for (int i = indexMarker; time.charAt(i) != 'S'; i++) {
            secondsValue += time.charAt(i);
        }
        seconds = parseFloat(secondsValue);

        result += hours * 3600;
        result += minutes * 60;
        result += seconds;

        return result;
    }
}

