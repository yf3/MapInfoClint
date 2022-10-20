package yf3.map_info.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.internal.EverythingIsNonNull

class POITypesRepository {
    fun getTypes(typeRequestListener: TypeRequestListener) {
        val retrofit = NetworkClient.getRetrofitClient()
        val getTypeAPI = retrofit.create(GetPOITypes::class.java)
        val call = getTypeAPI.allPOITypes
        call.enqueue(object : Callback<List<POITypeDataPair?>?> {
            @EverythingIsNonNull
            override fun onResponse(
                call: Call<List<POITypeDataPair?>?>,
                response: Response<List<POITypeDataPair?>?>
            ) {
                if (200 == response.code()) {
                    typeRequestListener.onSuccess(response.body())
                }
            }

            @EverythingIsNonNull
            override fun onFailure(call: Call<List<POITypeDataPair?>?>, t: Throwable) {
                typeRequestListener.onFailure()
            }
        })
    }

    interface TypeRequestListener {
        fun onSuccess(poiTypes: List<POITypeDataPair?>?)
        fun onFailure()
    }
}