package csie.ndhu.map_info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class GallerySharedViewModel extends AndroidViewModel {

    private POIRepository poiRepository;
    public GallerySharedViewModel(@NonNull Application application) {
        super(application);
        poiRepository = new POIRepository();
    }

    public void upload(POIArgs poiArgs) {
        poiRepository.uploadPOI(poiArgs);
    }
}