package yf3.map_info;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class POIRepository {

    public void uploadPOI(POIArgs poiArgs, UploadListener uploadListener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        PointOfInterest poi = new PointOfInterest(poiArgs);

        Call<ResponseBody> call = uploadAPIs.uploadImage(
                poi.mTitleStringBody,
                poi.mImagePart,
                poi.mLongitude,
                poi.mLatitude,
                poi.mCommentStringBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 400) {
                    try {
                        Log.i("Bad Request 400", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadListener.onBadRequest();
                }
                else {
                    Log.i("Response code", String.valueOf(response.code()));
                    uploadListener.onSuccess();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                uploadListener.onFailure();
            }
        });
    }

    interface UploadListener {
        void onSuccess();
        void onBadRequest();
        void onFailure();
    }
}
