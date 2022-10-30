package yf3.map_info.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PoiService {
    @GET("/poi/")
    fun getMapPOIs(): Call<List<PointOfInterest>>

    @GET("/poitypes/")
    fun getMapPoiTypes():  Call<List<POITypeDataPair>>

    @Multipart
    @POST("/poi/")
    fun upload(@Part("name") name: RequestBody,
               @Part attachment: MultipartBody.Part,
               @Part("longitude") longitude: Double,
               @Part("latitude") latitude: Double,
               @Part("poi_type") typeID: Int,
               @Part("comment") comment: RequestBody) : Call<ResponseBody>
}