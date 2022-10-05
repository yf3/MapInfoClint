package csie.ndhu.map_info;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class POIRepository {

    public void uploadPOI(POIArgs poiArgs) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        PointOfInterest poi = new PointOfInterest(poiArgs);

        Call call = uploadAPIs.uploadImage(
                poi.mTitleStringBody,
                poi.mImagePart,
                poi.mLongitude,
                poi.mLatitude,
                poi.mCommentStringBody);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 400) {
                    try {
                        Log.i("Bad Request 400", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // TODO: update UI state in viewmodel
                    // textViewResult.setText("Upload Failed!");
                }
                else {
                    Log.i("Response code", String.valueOf(response.code()));
                    // TODO: update UI state in viewmodel
                    // textViewResult.setText("Upload Success!");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                // TODO: update UI state in viewmodel
            }
        });
    }
}
