package csie.ndhu.map_info;

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

public class CameraViewModel extends AndroidViewModel implements LocationListener {
    private LocationManager locationRepository = null;
    private MutableLiveData<Location> instantLocation;
    public LiveData<Location> observedLocation;
    private boolean hasLocationPermissions = false;
    private PhotoModel photoModel;

    public CameraViewModel(@NonNull Application application) {
        super(application);
    }

    public void setupLocationFeature() {
        hasLocationPermissions = true;
        if (null == instantLocation) {
            instantLocation = new MutableLiveData<>();
        }
        observedLocation = instantLocation;
        locationRepository = LocationManager.getInstance(getApplication().getApplicationContext());
        locationRepository.registerListener(this);
    }

    public void setPhotoToAddLocation(File file) {
        if (null == photoModel) {
            photoModel = new PhotoModel(file);
        }
        photoModel.setFile(file);
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
                    // TODO: Lock capture UI button or combine the following 2 as one async step,
                    //  or the first taken photo will miss location info by rapidly press capture button
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
                "IMG_"+ timeStamp + PhotoModel.PHOTO_EXT);
        return mediaFile;
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
