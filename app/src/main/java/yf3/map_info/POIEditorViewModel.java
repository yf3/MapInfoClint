package yf3.map_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class POIEditorViewModel extends ViewModel {

    private final POIRepository poiRepository;
    private MutableLiveData<String> uploadStatus;

    private double longitude;
    private double latitude;
    private boolean isLocationInit = false;

    public POIEditorViewModel() {
        poiRepository = new POIRepository();
        uploadStatus = new MutableLiveData<>();
    }

    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }

    public void initPhotoLocation(String path) {
        if (false == isLocationInit) {
            LocationParser.LongLatPair longLatPair = PhotoModel.getLocationPair(path);
            longitude = longLatPair.longitude;
            latitude = longLatPair.latitude;
            isLocationInit = true;
        }
    }

    public void upload(POIArgs poiArgs) {
        uploadStatus.setValue("Uploading...");
        poiRepository.uploadPOI(poiArgs, new POIRepository.UploadListener() {
            @Override
            public void onSuccess() {
                uploadStatus.setValue("Done!");
            }

            @Override
            public void onBadRequest() {
                uploadStatus.setValue("Bad Request.");
            }

            @Override
            public void onFailure() {
                uploadStatus.setValue("Connection Error.");
            }
        });
    }

    public LiveData<String> getUploadStatus() {
        return uploadStatus;
    }
}