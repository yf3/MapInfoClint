package yf3.map_info;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class POIEditorViewModel extends ViewModel {

    private final POIRepository poiRepository;
    private MutableLiveData<String> uploadStatus;

    private MutableLiveData<List<POITypeDataPair>> poiTypes;
    private double longitude;
    private double latitude;
    private boolean isLocationInit = false;

    public POIEditorViewModel() {
        poiRepository = new POIRepository();
        uploadStatus = new MutableLiveData<>();
        poiTypes = new MutableLiveData<>();
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

    public void getExistedPOITypes() {
        poiRepository.getTypes(new POIRepository.TypeRequestListener() {
            @Override
            public void onSuccess(List<POITypeDataPair> poiTypesFromRepo) {
                poiTypes.setValue(poiTypesFromRepo);
            }

            @Override
            public void onFailure() {
                Log.e("POI VM", "failed getting POI types");
                List<POITypeDataPair> notFoundList = new ArrayList<>();
                notFoundList.add(new POITypeDataPair(Configs.UNSORTED_ID, "Unsorted"));
                poiTypes.setValue(notFoundList);
            }
        });
    }

    public void upload(POIArgs poiArgs) {
//        uploadStatus.setValue("Uploading...");
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

    public LiveData<List<POITypeDataPair>> getPOITypes() { return poiTypes; }
    public LiveData<String> getUploadStatus() {
        return uploadStatus;
    }
}