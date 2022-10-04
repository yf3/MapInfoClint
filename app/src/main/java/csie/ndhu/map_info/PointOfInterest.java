package csie.ndhu.map_info;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PointOfInterest {
    private String title;
    private File filePath;
    private double longitude;
    private double latitude;
    private String comment;

    private RequestBody getStringBody(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }
}
