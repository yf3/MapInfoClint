package yf3.map_info.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.*

class POITempRepository {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun requestGetPOIs():  Response<List<POISerializable>> {
        val retrofit: Retrofit = NetworkClient.getRetrofitClient()
        val apiInterface = retrofit.create(GetMapPOIs::class.java)
        val call = apiInterface.getMapPOIs()
        return withContext(dispatcher) {
            call.awaitResponse()
        }
    }
}