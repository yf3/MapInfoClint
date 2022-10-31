package yf3.map_info.data

sealed class RemoteApiResult<out R> {
    data class Success<out T>(val data: T) : RemoteApiResult<T>()
    data class Error(val exception: Exception) : RemoteApiResult<Nothing>()
}