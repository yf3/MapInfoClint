package csie.ndhu.mapInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;


public class LocationManager {

    private static LocationManager instance;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;
    private LocationCallback locationCallback;
    private LocationListener instantLocationListener;

    private LocationManager(Context appContext) {
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
        this.instantLocationListener = locationListener;
    }

    @SuppressLint("MissingPermission")
    public void findCurrentLocation() {
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        CancellationToken cancellationToken = cancellationTokenSource.getToken();
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken).addOnSuccessListener(
                location -> {
                    if (location != null) {
                        lastLocation = location;
                        Log.i("Location", "Location is found.");
                        Log.i("Location", String.format("%f, %f", lastLocation.getLongitude(), lastLocation.getLatitude()));
                        instantLocationListener.onLocationFound();
                    }
                    else {
                        Log.i("Location", "Location is null.");
                    }
                }).addOnFailureListener(
                location -> {
                    cancellationTokenSource.cancel();
                });
    }

    @SuppressLint("MissingPermission")
    public void updateAndNotify() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(0)
                .setNumUpdates(1);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
                Log.i("LocationCallback", String.format("%f, %f", lastLocation.getLongitude(), lastLocation.getLatitude()));
            }
        };
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
