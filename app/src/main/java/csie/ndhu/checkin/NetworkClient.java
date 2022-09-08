package csie.ndhu.checkin;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkClient {

    private static String BASE_URL = "http://" + R.string.local_address + ":" + R.string.local_port;
    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {

        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
