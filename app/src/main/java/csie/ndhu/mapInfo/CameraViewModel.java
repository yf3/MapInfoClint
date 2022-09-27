package csie.ndhu.mapInfo;

import android.app.Application;
import android.location.Location;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CameraViewModel extends AndroidViewModel implements LocationListener {
    private LocationManager locationRepository = null;
    MutableLiveData<Location> instantLocation;
    private PhotoModel photoModel;

    public CameraViewModel(@NonNull Application application) {
        super(application);
    }

    public void setupLocationRepo() {
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

    public MutableLiveData<Location> getInstantLocation() {
        if (null == instantLocation) {
            instantLocation = new MutableLiveData<>();
        }
        return instantLocation;
    }


    @Override
    public void onLocationFound() {
        instantLocation.setValue(locationRepository.getCurrentLocation());
        if (null != photoModel) {
            photoModel.writeLocationExif(instantLocation.getValue());
        }
    }
}
