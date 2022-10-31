package yf3.map_info.data

import retrofit2.*

class MapPoiRepository {

    suspend fun requestPOIsList():  RemoteApiResult<Response<List<PointOfInterest>>> {
        val retrofit: Retrofit = NetworkClient.getRetrofitClient()
        val apiInterface = retrofit.create(PoiService::class.java)
        val call = apiInterface.getMapPOIs()
        return RemoteApiResult.handleApiResult(call)
    }
}