package csie.ndhu.mapInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class LocationManager {

    private static LocationManager instance;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;
    private LocationCallback locationCallback;
    private List<LocationListener> locationListeners;
    // TODO: better off not using this, other way to observe

    private LocationManager(Context appContext) {
        locationListeners = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext);
    }

    public static LocationManager getInstance(Context appContext) {
        if (null == instance) {
            synchronized (LocationManager.class) {
                if (null == instance) {
                    instance = new LocationManager(appContext);
                }
            }
        }
        return instance;
    }

    public void registerListener(LocationListener locationListener) {
        locationListeners.add(locationListener);
    }

    @SuppressLint("MissingPermission")
    public void findLastLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lastLocation = location;
                            Log.i("Location", "Location is found.");
                            Log.i("Location", String.format("%f, %f", lastLocation.getLongitude(), lastLocation.getLatitude()));
                        }
                        else {
                            Log.i("Location", "Location is null.");
                        }
                    }
                });

        fusedLocationProviderClient.getLastLocation().addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Location", "Location failed.");
                    }
                }
            );
    }

    @SuppressLint("MissingPermission")
    public void updateAndNotify() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
                Log.i("LocationCallback", String.format("%f, %f", lastLocation.getLongitude(), lastLocation.getLatitude()));
                for (LocationListener locationListener: locationListeners) {
                    locationListener.onLocationFound();
                }
            }
        };
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public double getLongitude()
    {
        if (lastLocation != null) {
            return lastLocation.getLongitude();
        }
        else {
            return 121.4;
        }
    }

    public double getLatitude() {
        if (lastLocation != null) {
            return lastLocation.getLatitude();
        }
        else {
            return 23.9;
        }
    }
}
