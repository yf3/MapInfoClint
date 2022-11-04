package yf3.map_info.ui;

import android.app.Application;
import android.location.Location;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import yf3.map_info.data.LocationListener;
import yf3.map_info.data.LocationManager;
import yf3.map_info.util.PhotoExif;

public class CameraViewModel extends AndroidViewModel implements LocationListener {
    private LocationManager locationRepository = null;
    private MutableLiveData<Location> instantLocation;
    private boolean hasLocationPermissions = false;
    private PhotoExif photoExif;

    public CameraViewModel(@NonNull Application application) {
        super(application);
    }

    public void setupLocationFeature() {
        hasLocationPermissions = true;
        if (null == instantLocation) {
            instantLocation = new MutableLiveData<>();
        }
        locationRepository = LocationManager.getInstance(getApplication().getApplicationContext());
        locationRepository.registerListener(this);
    }

    public void setPhotoToAddLocation(File file) {
        if (null == photoExif) {
            photoExif = new PhotoExif(file);
        }
        photoExif.setFile(file);
    }

    public void takePhoto(ImageCapture imageCapture) {
        Executor takePictureExecutor = Executors.newSingleThreadExecutor();
        File imageFile = getInternalImageFile();
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(imageFile).build();
        imageCapture.takePicture(outputFileOptions, takePictureExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // TODO: Consider adjusting rotation
                if (hasLocationPermissions) {
                    setPhotoToAddLocation(imageFile);
                    requestCurrentLocation();
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });
    }

    private File getInternalImageFile() {
        File mediaStorageDir = getApplication().getFilesDir();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + PhotoExif.PHOTO_EXT);
        return mediaFile;
    }

    public void requestCurrentLocation() {
        locationRepository.findCurrentLocation();
    }

    @Override
    public void onLocationFound() {
        instantLocation.setValue(locationRepository.getCurrentLocation());
        if (null != photoExif) {
            photoExif.writeLocationExif(instantLocation.getValue());
        }
    }

    public LiveData<Location> getObservedLocation() {
        return instantLocation;
    }
}
