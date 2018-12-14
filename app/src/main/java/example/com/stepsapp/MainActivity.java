package example.com.stepsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    static public SQLiteDatabase activityLog = null;

    final static public SimpleDateFormat printDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    //public TextView count;
    //public TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MovementFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_movement);
        }
        activityLog = (new DatabaseHelper(this)).getWritableDatabase();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_current_activity:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentActivityFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_movement:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MovementFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_activity_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ActivityHistoryFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_charts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChartsFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
