package example.com.stepsapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private PackageManager packageManager;

    boolean activityRunning;

    public TextView count;
    public TextView state;
    public TextView timerDisplay;

    private CountDownTimer timer;
    private String stateMessage;

    private long timeLeftInMilli = 5000;
    private double secondsWalking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        packageManager = getPackageManager();

        count = (TextView)findViewById(R.id.count);
        state = (TextView)findViewById(R.id.state);


        stateMessage = "Standing";
        state.setText(stateMessage);

      //  updateTimer();
        secondsWalking = 0;




    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;


       Sensor counterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

       if (counterSensor != null) {
           Toast.makeText(this, "TENGO UN STEP COUNTER USANDO SENSOR MANAGER", Toast.LENGTH_LONG).show();
           mSensorManager.registerListener(this, counterSensor, SensorManager.SENSOR_DELAY_NORMAL);


       }


    }

    @Override
    protected void onPause() {
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
