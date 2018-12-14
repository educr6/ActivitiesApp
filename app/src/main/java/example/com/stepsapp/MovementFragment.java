package example.com.stepsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MovementFragment extends Fragment  implements SensorEventListener {

    private SensorManager mSensorManager;
    private PackageManager packageManager;

    boolean activityRunning;

    private DrawerLayout drawer;

    public TextView count;
    public TextView countReference;

    public TextView state;


    private String stateMessage;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    private float secondsWalking;
    private float foregroundSteps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movement, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        packageManager = getActivity().getPackageManager();

        count = (TextView)getActivity().findViewById(R.id.count);
        state = (TextView)getActivity().findViewById(R.id.state);
        countReference = (TextView)getActivity().findViewById(R.id.countReference);


        stateMessage = "Standing";
        state.setText(stateMessage);

        //  updateTimer();
        secondsWalking = 0;

        prefs = getActivity().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = prefs.edit();



    }


    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;


        Sensor counterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (counterSensor != null) {
           // Toast.makeText(getContext(), "TENGO UN STEP COUNTER USANDO SENSOR MANAGER", Toast.LENGTH_LONG).show();
            mSensorManager.registerListener(this, counterSensor, SensorManager.SENSOR_DELAY_NORMAL);


        }




    }

    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;





        // timer.cancel();
        // if you unregister the last listener, the hardware will stop detecting step events
        //9  mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {

            float numberOfSteps = Float.parseFloat(String.valueOf(event.values[0]));
            float reset = prefs.getFloat("reset", -1);
            secondsWalking = prefs.getFloat("SecondsOnTheGo", -1);

            if (reset == -1 ) {

                editor.putFloat("reference", numberOfSteps);
                editor.putFloat("reset", 0);
                editor.apply();

                secondsWalking = 0;
                foregroundSteps = 0;
            }

            if (secondsWalking == -1) {
                secondsWalking = 0;
            }

            float reference = prefs.getFloat("reference", 0);

           float currentSteps = numberOfSteps - reference;
            secondsWalking += 1.915;
            secondsWalking = round(secondsWalking, 2);


            count.setText(Float.toString(numberOfSteps));
            countReference.setText(Float.toString(currentSteps));
            state.setText("Seconds on the go: " + secondsWalking);

            editor.putFloat("steps", currentSteps);
            editor.putFloat("LastRecord", numberOfSteps);
            editor.putFloat("SecondsOnTheGo", secondsWalking);
            editor.apply();



        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }





    public static float round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

}
