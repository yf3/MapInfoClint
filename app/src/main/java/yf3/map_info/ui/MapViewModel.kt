package yf3.map_info.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import yf3.map_info.data.POISerializable
import yf3.map_info.data.POITempRepository

class MapViewModel: ViewModel() {
    private var poiTempRepository = POITempRepository()
    private var _poiList = MutableLiveData<List<POISerializable>?>()
    val poiList get() = _poiList

    fun getMapPOIs() {
        viewModelScope.launch {
            val result = poiTempRepository.requestPOIsList()
            if (result.isSuccessful) {
                _poiList.postValue(result.body())
                for (poi in result.body()!!) {
                    Log.i("POI", poi.photoRelativePath)
                }
            }
        }
    }
}