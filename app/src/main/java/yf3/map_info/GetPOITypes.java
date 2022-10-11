package yf3.map_info;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetPOITypes {
    @GET("/poitypes/")
    Call<List<POITypeDataPair>> getAllPOITypes();
}


