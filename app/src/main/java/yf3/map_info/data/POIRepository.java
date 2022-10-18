package yf3.map_info.data;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import yf3.map_info.util.POIArgs;

public class POIRepository {

    public void getTypes(TypeRequestListener typeRequestListener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        GetPOITypes getTypeAPI = retrofit.create(GetPOITypes.class);

        Call<List<POITypeDataPair>> call = getTypeAPI.getAllPOITypes();

        call.enqueue(new Callback<List<POITypeDataPair>>() {
            @Override
            public void onResponse(Call<List<POITypeDataPair>> call, Response<List<POITypeDataPair>> response) {
                if (200 == response.code()) {
                    typeRequestListener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<POITypeDataPair>> call, Throwable t) {
                typeRequestListener.onFailure();
            }
        });
    }

    public void uploadPOI(POIArgs poiArgs, UploadListener uploadListener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        PointOfInterest poi = new PointOfInterest(poiArgs);

        Call<ResponseBody> call = uploadAPIs.uploadImage(
                poi.mTitleStringBody,
                poi.mImagePart,
                poi.mLongitude,
                poi.mLatitude,
                poi.mTypeID,
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

    public interface TypeRequestListener {
        void onSuccess(List<POITypeDataPair> poiTypes);
        void onFailure();
    }

    public interface UploadListener {
        void onSuccess();
        void onBadRequest();
        void onFailure();
    }
}
