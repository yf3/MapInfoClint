package yf3.map_info.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;


public class LocationManager {

    private static LocationManager instance;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
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
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken)
                .addOnSuccessListener(
                location -> {
                    if (location != null) {
                        currentLocation = location;
                        Log.i("LocationManager", "Location is found.");
                        Log.i("LocationManager", String.format("%f, %f", currentLocation.getLongitude(), currentLocation.getLatitude()));
                        instantLocationListener.onLocationFound();
                    }
                    else {
                        Log.i("LocationManager", "Location is null.");
                    }
                }).addOnFailureListener(
                location -> {
                    cancellationTokenSource.cancel();
                });
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
