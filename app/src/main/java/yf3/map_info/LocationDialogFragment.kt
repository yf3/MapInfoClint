package yf3.map_info

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.scale
import androidx.navigation.fragment.findNavController
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import yf3.map_info.databinding.FragmentLocationDialogBinding


class LocationDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLocationDialogBinding? = null
    private val binding get() = _binding!!
    private var mLong: Double? = null
    private var mLat: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mLong = LocationDialogFragmentArgs.fromBundle(it).longitude.toDouble()
            mLat = LocationDialogFragmentArgs.fromBundle(it).latitude.toDouble()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentLocationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cameraInitOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(mLong!!, mLat!!))
            .zoom(15.0)
            .build()

        var markerBmp:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        markerBmp.scale(markerBmp.width/2, markerBmp.height/2).also { markerBmp = it }
        val annotationApi = binding.mapView.annotations
        val pointAnnotationManager = annotationApi?.createPointAnnotationManager()
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(mLong!!, mLat!!))
            .withIconImage(markerBmp)
        pointAnnotationManager?.create(pointAnnotationOptions)

        binding.mapView.getMapboxMap().setCamera(cameraInitOptions)
        binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

        binding.backFAB.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}