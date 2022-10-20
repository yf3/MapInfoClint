package yf3.map_info.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yf3.map_info.data.POISerializable
import yf3.map_info.data.POITempRepository

class MapViewModel: ViewModel() {
    private var poiTempRepository = POITempRepository()
    private var _poiList = MutableLiveData<List<POISerializable>?>()
    val poiList get() = _poiList

    fun getPOIsForMap() {
        poiTempRepository.getMapPOIs(object: POITempRepository.POIRequestListener{
            override fun onSuccess(receivedPoiList: List<POISerializable>?) {
                _poiList.postValue(receivedPoiList)
            }

            override fun onFailure() {
                TODO("Not yet implemented")
            }
        })
    }
}