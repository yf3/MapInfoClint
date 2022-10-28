package yf3.map_info.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.*

class MapPoiRepository {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun requestPOIsList():  Response<List<PointOfInterest>> {
        val retrofit: Retrofit = NetworkClient.getRetrofitClient()
        val apiInterface = retrofit.create(PoiService::class.java)
        val call = apiInterface.getMapPOIs()
        return withContext(dispatcher) {
            call.awaitResponse()
        }
    }
}