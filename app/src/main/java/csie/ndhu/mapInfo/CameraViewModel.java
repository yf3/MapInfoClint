package csie.ndhu.mapInfo;

import android.app.Application;
import android.location.Location;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CameraViewModel extends AndroidViewModel implements LocationListener {
    private LocationManager locationRepository = null;
    MutableLiveData<Location> instantLocation;
    private File photoToAddLocation;

    public CameraViewModel(@NonNull Application application) {
        super(application);
    }

    public void setupLocationRepo() {
        locationRepository = LocationManager.getInstance(getApplication().getApplicationContext());
        locationRepository.registerListener(this);
    }

    public void setPhotoToAddLocation(File file) {
        photoToAddLocation = file;
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

    private void writeLocationExif() {
        final ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(photoToAddLocation.getAbsolutePath());
            String locationDescription = "Longitude:" + instantLocation.getValue().getLongitude() +
                    " " + "Latitude:" + instantLocation.getValue().getLatitude();
                    exifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, locationDescription);
            exifInterface.saveAttributes();
            Log.i("exif", "" + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION));
        } catch (FileNotFoundException e) {
            Log.d(null, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(null, "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    public void onLocationFound() {
        instantLocation.setValue(locationRepository.getCurrentLocation());
        if (null != photoToAddLocation) {
            writeLocationExif();
        }
    }
}
