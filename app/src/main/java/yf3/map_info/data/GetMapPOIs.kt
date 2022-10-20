package yf3.map_info.data

import retrofit2.Call
import retrofit2.http.GET
import yf3.map_info.data.POISerializable

interface GetMapPOIs {
    @GET("/poi/")
    fun getMapPOIs(): Call<List<POISerializable>>
}