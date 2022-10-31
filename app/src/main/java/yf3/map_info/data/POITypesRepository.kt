package yf3.map_info.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse

class POITypesRepository {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getPoiTypes(): RemoteApiResult<Response<List<POITypeDataPair>>> {
        val retrofit = NetworkClient.getRetrofitClient()
        val getTypeAPI = retrofit.create(PoiService::class.java)
        val call = getTypeAPI.getMapPoiTypes()
        return try {
            withContext(dispatcher) {
                RemoteApiResult.Success(call.awaitResponse())
            }
        } catch (e: Exception) {
            RemoteApiResult.Error(e)
        }
    }
}