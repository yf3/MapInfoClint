package yf3.map_info

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import yf3.map_info.databinding.FragmentViewPhotoBinding

class ViewPhotoFragment: Fragment() {

    private var _binding: FragmentViewPhotoBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentViewPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mainImageView.setImageDrawable(Drawable.createFromPath(photoPath))
        binding.uploadFAB.setOnClickListener {
            Navigation.findNavController(it).navigate(ViewPhotoFragmentDirections.actionViewPhotoFragmentToPOIEditFragment(photoPath))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}