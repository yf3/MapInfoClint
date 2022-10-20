package yf3.map_info.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import yf3.map_info.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding:FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var mLong: Double = 121.51797666246986 // Test Purpose
    private var mLat: Double = 25.027184623416378 // Test Purpose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

}