package example.com.stepsapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class MovementFragment extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager;
    private PackageManager packageManager;

    boolean activityRunning;

    private DrawerLayout drawer;

    public TextView count;
    public TextView state;
    public TextView timerDisplay;

    private CountDownTimer timer;
    private String stateMessage;

    private long timeLeftInMilli = 5000;
    private double secondsWalking;

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


        stateMessage = "Standing";
        state.setText(stateMessage);

        //  updateTimer();
        secondsWalking = 0;



    }


    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;


        Sensor counterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (counterSensor != null) {
            Toast.makeText(getContext(), "TENGO UN STEP COUNTER USANDO SENSOR MANAGER", Toast.LENGTH_LONG).show();
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
            count.setText(String.valueOf(event.values[0]));
            secondsWalking += 1.915;
            secondsWalking = round(secondsWalking, 2);

            state.setText("Seconds on the go: " + secondsWalking);

            //  if (stateMessage != "Moving") {
            //    stateMessage  = "Moving";
            //   state.setText(stateMessage);

            //}


            //  StartTimer();

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void StartTimer() {


        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilli = millisUntilFinished;
                //updateTimer();

            }

            @Override
            public void onFinish() {
                stateMessage = "Standing";
                state.setText(stateMessage);


            }
        }.start();
    }

    private void updateTimer() {
        int seconds = (int) timeLeftInMilli / 1000;
        //timerDisplay.setText("" + seconds);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
