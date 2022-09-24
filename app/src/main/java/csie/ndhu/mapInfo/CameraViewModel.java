package csie.ndhu.mapInfo;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CameraViewModel extends AndroidViewModel implements LocationListener {
    private LocationManager locationRepository = null;
    MutableLiveData<Location> instantLocation;

    public CameraViewModel(@NonNull Application application) {
        super(application);
    }

    public void setupLocationRepo() {
        locationRepository = LocationManager.getInstance(getApplication().getApplicationContext());
        locationRepository.registerListener(this);
    }

    public void updateCurrentLocation() {
        locationRepository.findCurrentLocation();
    }

    public MutableLiveData<Location> getInstantLocation() {
        if (null == instantLocation) {
            instantLocation = new MutableLiveData<>();
        }
        return instantLocation;
    }

    @Override
    public void onLocationFound() {
        instantLocation.setValue(locationRepository.getCurrentLocation());
    }
}
