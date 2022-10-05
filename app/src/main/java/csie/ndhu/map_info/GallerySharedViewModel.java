package csie.ndhu.map_info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GallerySharedViewModel extends AndroidViewModel {

    private POIRepository poiRepository;
    private MutableLiveData<String> uploadStatus;

    public GallerySharedViewModel(@NonNull Application application) {
        super(application);
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