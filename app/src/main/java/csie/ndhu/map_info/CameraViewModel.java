package csie.ndhu.map_info;

import android.app.Application;
import android.location.Location;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CameraViewModel extends AndroidViewModel implements LocationListener {
    private LocationManager locationRepository = null;
    private MutableLiveData<Location> instantLocation;
    public LiveData<Location> observedLocation;
    private PhotoModel photoModel;

    public CameraViewModel(@NonNull Application application) {
        super(application);
    }

    public void setupLocationRepo() {
        if (null == instantLocation) {
            instantLocation = new MutableLiveData<>();
        }
        observedLocation = instantLocation;
        locationRepository = LocationManager.getInstance(getApplication().getApplicationContext());
        locationRepository.registerListener(this);
    }

    public void setPhotoToAddLocation(File file) {
        if (null == photoModel) {
            photoModel = new PhotoModel();
        }
        photoModel.setFile(file);
    }

    public void requestCurrentLocation() {
        locationRepository.findCurrentLocation();
    }

    @Override
    public void onLocationFound() {
        instantLocation.setValue(locationRepository.getCurrentLocation());
        if (null != photoModel) {
            photoModel.writeLocationExif(instantLocation.getValue());
        }
    }

    public LiveData<Location> getObservedLocation() {
        return observedLocation;
    }
}
