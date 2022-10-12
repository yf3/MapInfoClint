package yf3.map_info

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


class LocationDialogFragment : BottomSheetDialogFragment() {

    private var mapView: MapView? = null
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
        return inflater.inflate(R.layout.fragment_location_dialog_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.mapView)
        val cameraInitOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(mLong!!, mLat!!))
            .zoom(15.0)
            .build()
        val annotationApi = mapView?.annotations
        val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(mLong!!, mLat!!))
            .withIconImage(convertDrawableToBitmap(getDrawable(requireContext(), R.drawable.red_marker))!!)
        pointAnnotationManager?.create(pointAnnotationOptions)

        mapView?.getMapboxMap()?.setCamera(cameraInitOptions)
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)

        val backFab: FloatingActionButton? = view.findViewById(R.id.backFAB)
        backFab?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}