package yf3.map_info

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewPhotoFragment: Fragment() {

    private var photoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoPath = ViewPhotoFragmentArgs.fromBundle(it).filePath
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_photo, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageView: ImageView? = view.findViewById(R.id.mainImageView)
        imageView?.setImageDrawable(Drawable.createFromPath(photoPath))
        val uploadFAB: FloatingActionButton? = view.findViewById(R.id.uploadFAB)
        uploadFAB?.setOnClickListener {
            Navigation.findNavController(it).navigate(ViewPhotoFragmentDirections.actionViewPhotoFragmentToPOIEditFragment(photoPath))
        }
    }

}