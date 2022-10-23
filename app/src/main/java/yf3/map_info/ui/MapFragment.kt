package yf3.map_info.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModelProvider
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import yf3.map_info.R
import yf3.map_info.data.POISerializable
import yf3.map_info.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding:FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var viewModel: MapViewModel? = null

    private var mLong: Double = 121.51797666246986 // Test Purpose
    private var mLat: Double = 25.027184623416378 // Test Purpose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cameraInitOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(mLong, mLat))
            .zoom(9.0)
            .build()
        binding.mapView.getMapboxMap().setCamera(cameraInitOptions)
        binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        viewModel!!.getMapPOIs()
        viewModel!!.poiList.observe(viewLifecycleOwner) {
            addPOIAnnotations(it)
        }
    }

    private fun addPOIAnnotations(poiList: List<POISerializable>?) {
        var markerBmp: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        markerBmp.scale(markerBmp.width/2, markerBmp.height/2).also { markerBmp = it }
        val annotationApi = binding.mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        val pointAnnotationOptionsList: MutableList<PointAnnotationOptions> = mutableListOf()
        for (poi in poiList!!) {
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(poi.longitude, poi.latitude))
                .withIconImage(markerBmp)
            pointAnnotationOptionsList.add(pointAnnotationOptions)
        }
        pointAnnotationManager.create(pointAnnotationOptionsList)
    }

}