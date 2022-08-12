package csie.ndhu.checkin.data.model;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("key")
    private String key;
    public String getKey() {
        return key;
    }
}
