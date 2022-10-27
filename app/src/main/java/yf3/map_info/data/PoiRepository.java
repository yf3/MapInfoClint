package yf3.map_info.data;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;
import yf3.map_info.util.POIArgs;

public class PoiRepository {

    public void uploadPOI(POIArgs poiArgs, UploadListener uploadListener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        PoiRequestWrapper poi = new PoiRequestWrapper(poiArgs);

        Call<ResponseBody> call = uploadAPIs.upload(
                poi.mTitleStringBody,
                poi.mImagePart,
                poi.mLongitude,
                poi.mLatitude,
                poi.mTypeID,
                poi.mCommentStringBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
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
            @EverythingIsNonNull
            public void onFailure(Call call, Throwable t) {
                uploadListener.onFailure();
            }
        });
    }



    public interface UploadListener {
        void onSuccess();
        void onBadRequest();
        void onFailure();
    }
}
