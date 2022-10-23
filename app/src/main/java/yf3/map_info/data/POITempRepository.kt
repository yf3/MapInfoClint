package yf3.map_info.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class POITempRepository {

    val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun getMapPOIs(poiRequestListener: POIRequestListener) {
        val retrofit: Retrofit = NetworkClient.getRetrofitClient()
        val apiInterface = retrofit.create(GetMapPOIs::class.java)
        val call = apiInterface.getMapPOIs()
        call.enqueue(object: Callback<List<POISerializable>> {
            override fun onResponse(
                call: Call<List<POISerializable>>,
                response: Response<List<POISerializable>>
            ) {
                poiRequestListener.onSuccess(response.body())
            }

            override fun onFailure(call: Call<List<POISerializable>>, t: Throwable) {
                poiRequestListener.onFailure()
            }
        })
    }

    suspend fun requestGetPOIs() {
        val retrofit: Retrofit = NetworkClient.getRetrofitClient()
        val apiInterface = retrofit.create(GetMapPOIs::class.java)
        val call = apiInterface.getMapPOIs()
        return withContext(dispatcher) {
            call.execute()
        }
    }

    interface POIRequestListener {
        fun onSuccess(receivedPoiList: List<POISerializable>?)
        fun onFailure()
    }
}