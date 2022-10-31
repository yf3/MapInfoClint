package yf3.map_info.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import yf3.map_info.data.PointOfInterest
import yf3.map_info.data.MapPoiRepository
import yf3.map_info.data.RemoteApiResult

class MapViewModel: ViewModel() {
    private var mapPoiRepository = MapPoiRepository()
    private var _poiList = MutableLiveData<List<PointOfInterest>?>()
    val poiList get() = _poiList

    fun getMapPOIs() {
        viewModelScope.launch {
            val result = mapPoiRepository.requestPOIsList()
            if (result is RemoteApiResult.Success) {
                val receivedList = result.data.body()
                _poiList.postValue(receivedList)
                for (poi in receivedList!!) {
                    Log.i("POI", poi.photoRelativePath)
                }
            }
        }
    }
}