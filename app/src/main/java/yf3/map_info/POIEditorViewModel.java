package yf3.map_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class POIEditorViewModel extends ViewModel {

    private POIRepository poiRepository;
    private MutableLiveData<String> uploadStatus;

    public POIEditorViewModel() {
        poiRepository = new POIRepository();
        uploadStatus = new MutableLiveData<>();
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