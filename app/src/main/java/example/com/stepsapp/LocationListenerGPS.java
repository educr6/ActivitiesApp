package example.com.stepsapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationListenerGPS implements LocationListener {
    public Location currentLocation = null;
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
