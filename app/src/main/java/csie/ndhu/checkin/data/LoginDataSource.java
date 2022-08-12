package csie.ndhu.checkin.data;

import android.util.Log;

import org.json.JSONObject;

import csie.ndhu.checkin.NetworkClient;
import csie.ndhu.checkin.data.model.LoggedInUser;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        AuthAPIs authAPIs = retrofit.create(AuthAPIs.class);
        try {
            Call call = authAPIs.login(username, password);
            JSONObject responseJSON = new JSONObject(call.execute().body().toString());
            Log.i("LoginResponse", responseJSON.getString("auth_token"));
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            responseJSON.getString("auth_token"));
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
