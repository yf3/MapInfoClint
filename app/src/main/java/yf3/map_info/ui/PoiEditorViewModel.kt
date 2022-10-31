package yf3.map_info.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import yf3.map_info.Configs
import yf3.map_info.data.POITypeDataPair
import yf3.map_info.data.POITypesRepository
import yf3.map_info.data.PoiRequestRepository
import yf3.map_info.data.RemoteApiResult
import yf3.map_info.util.POIArgs
import yf3.map_info.util.PhotoModel

class PoiEditorViewModel: ViewModel() {
    private val requestRepository: PoiRequestRepository = PoiRequestRepository()
    private val poiTypesRepository: POITypesRepository = POITypesRepository()
    private var _uploadStatus: MutableLiveData<String> = MutableLiveData<String>()
    val uploadStatus get() = _uploadStatus

    private var _poiTypes: MutableLiveData<List<POITypeDataPair>> = MutableLiveData()
    val poiTypes get() = _poiTypes

    var longitude: Double? = null
        private set
    var latitude: Double? = null
        private set
    private var isLocationInit = false

    fun initPhotoLocation(path: String?) {
        if (!isLocationInit) {
            val longLatPair = PhotoModel.getLocationPair(path)
            longitude = longLatPair.longitude
            latitude = longLatPair.latitude
            isLocationInit = true
        }
    }

    fun getExistedPOITypes() {
        viewModelScope.launch {
            val result = poiTypesRepository.getPoiTypes()
            if (result is RemoteApiResult.Success) {
                _poiTypes.postValue(result.data.body())
            }
            else {
                Log.e("POI VM", "failed getting POI types")
                val notFoundList: MutableList<POITypeDataPair> = ArrayList()
                notFoundList.add(POITypeDataPair(Configs.UNSORTED_ID, "Unsorted"))
                _poiTypes.postValue(notFoundList)
            }
        }
    }

    fun makeUploadRequest(poiArgs: POIArgs) {
        viewModelScope.launch {
            when (requestRepository.uploadPoi(poiArgs)) {
                is RemoteApiResult.Success -> uploadStatus.postValue("New POI created.")
                is RemoteApiResult.Bad -> uploadStatus.postValue("Bad Request.")
                else -> {
                    uploadStatus.postValue("Connection Error.")
                }
            }
        }
    }
}