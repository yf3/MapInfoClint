package yf3.map_info.data

import okhttp3.ResponseBody
import retrofit2.*
import yf3.map_info.util.POIArgs

class MapPoiRepository {

    private val retrofit: Retrofit = NetworkClient.getRetrofitClient()
    private val apiInterface = retrofit.create(PoiService::class.java)

    suspend fun requestPOIsList():  RemoteApiResult<Response<List<PointOfInterest>>> {
        val call = apiInterface.getMapPOIs()
        return RemoteApiResult.handleApiResult(call)
    }

    suspend fun getPoiTypes(): RemoteApiResult<Response<List<POITypeDataPair>>> {
        val call = apiInterface.getMapPoiTypes()
        return RemoteApiResult.handleApiResult(call)
    }

    suspend fun uploadPoi(poiFormWrapper: POIArgs): RemoteApiResult<Response<ResponseBody>> {
        val requestWrapper = PoiRequestWrapper(poiFormWrapper)

        val call = apiInterface.upload(
            requestWrapper.mTitleStringBody,
            requestWrapper.mImagePart,
            requestWrapper.mLongitude,
            requestWrapper.mLatitude,
            requestWrapper.mTypeID,
            requestWrapper.mCommentStringBody
        )

        return RemoteApiResult.handleApiResult(call)
    }
}