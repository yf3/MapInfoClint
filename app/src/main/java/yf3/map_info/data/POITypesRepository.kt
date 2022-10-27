package yf3.map_info.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import retrofit2.internal.EverythingIsNonNull

class POITypesRepository {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getPoiTypes(): Response<List<POITypeDataPair>> {
        val retrofit = NetworkClient.getRetrofitClient()
        val getTypeAPI = retrofit.create(GetPOITypes::class.java)
        val call = getTypeAPI.allPOITypes
        return withContext(dispatcher) {
            call.awaitResponse()
        }
    }
}