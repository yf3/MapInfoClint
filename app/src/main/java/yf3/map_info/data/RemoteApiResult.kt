package yf3.map_info.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse

sealed class RemoteApiResult<out R> {
    data class Success<out T>(val data: T) : RemoteApiResult<T>()
    data class Bad(val code: Int): RemoteApiResult<Nothing>()
    data class Error(val exception: Exception) : RemoteApiResult<Nothing>()

    companion object {
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO
        suspend fun<T> handleApiResult(call: Call<T>): RemoteApiResult<Response<T>> {
            return try {
                withContext(dispatcher) {
                    val response = call.awaitResponse()
                    if (response.isSuccessful) {
                        Success(response)
                    }
                    else {
                        Bad(response.code())
                    }
                }
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}