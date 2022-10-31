package yf3.map_info.data

import okhttp3.ResponseBody
import retrofit2.Response

import yf3.map_info.util.POIArgs

class PoiRequestRepository {
    suspend fun uploadPoi(poiFormWrapper: POIArgs): RemoteApiResult<Response<ResponseBody>> {
        val retrofit = NetworkClient.getRetrofitClient()
        val uploadAPIs = retrofit.create(PoiService::class.java)

        val requestWrapper = PoiRequestWrapper(poiFormWrapper)

        val call = uploadAPIs.upload(
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