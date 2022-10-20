package yf3.map_info.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class POITempRepository {
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

    interface POIRequestListener {
        fun onSuccess(poiList: List<POISerializable>?)
        fun onFailure()
    }
}