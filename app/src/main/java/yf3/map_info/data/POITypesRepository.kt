package yf3.map_info.data

import retrofit2.Response

class POITypesRepository {

    suspend fun getPoiTypes(): RemoteApiResult<Response<List<POITypeDataPair>>> {
        val retrofit = NetworkClient.getRetrofitClient()
        val getTypeAPI = retrofit.create(PoiService::class.java)
        val call = getTypeAPI.getMapPoiTypes()
        return RemoteApiResult.handleApiResult(call)
    }
}