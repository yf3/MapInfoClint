package yf3.map_info.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.awaitResponse

import yf3.map_info.util.POIArgs

class PoiRequestRepository {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun uploadPoi(poiFormWrapper: POIArgs): Response<ResponseBody> {
        val retrofit = NetworkClient.getRetrofitClient()
        val uploadAPIs = retrofit.create(UploadAPIs::class.java)

        val requestWrapper = PoiRequestWrapper(poiFormWrapper)

        val call = uploadAPIs.upload(
            requestWrapper.mTitleStringBody,
            requestWrapper.mImagePart,
            requestWrapper.mLongitude,
            requestWrapper.mLatitude,
            requestWrapper.mTypeID,
            requestWrapper.mCommentStringBody
        )

        return withContext(dispatcher) {
            call.awaitResponse()
        }
    }
}