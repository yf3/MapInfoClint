package csie.ndhu.checkin.data;

import csie.ndhu.checkin.data.model.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthAPIs {
    @FormUrlEncoded
    @POST("/accounts/rest-auth/login/")
    Call<TokenResponse> login(@Field("username") String username,
                              @Field("password") String password);

}
