package csie.ndhu.mapInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPIs {

    @Multipart
    @POST("/poi/")
    Call<ResponseBody> uploadImage(@Part("name") RequestBody name, // String Request Body
                                   @Part MultipartBody.Part attachment,
                                   @Part("longitude") Double longitude,
                                   @Part("latitude") Double latitude,
                                   @Part("comment") RequestBody comment); // String Request Body
}