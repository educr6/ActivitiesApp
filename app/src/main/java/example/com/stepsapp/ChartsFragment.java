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

import java.util.ArrayList;

public class ChartsFragment extends Fragment {

    PieChart stepschart;
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



    }
}
