package yf3.map_info.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import yf3.map_info.*
import yf3.map_info.data.POITypeDataPair
import yf3.map_info.databinding.FragmentPoiEditBinding
import yf3.map_info.util.POIArgs

class POIEditFragment : Fragment() {

    private var _binding: FragmentPoiEditBinding? = null
    private val binding get() = _binding!!
    private var viewModel: PoiEditorViewModel? = null
    private var attachmentPath: String? = null
    private var selectedType: POITypeDataPair? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PoiEditorViewModel::class.java]
        arguments?.let {
            this.attachmentPath = POIEditFragmentArgs.fromBundle(it).attachmentPath
        }
        if (null != this.attachmentPath) {
            viewModel!!.initPhotoLocation(attachmentPath)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoiEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.uploadProgress.visibility = View.INVISIBLE

        loadTypeSelector(binding.typeSelector)

        binding.locationText.text = viewModel?.let { String.format("(%.3f, %.3f)", it.longitude, it.latitude) }

        binding.locationIcon.setOnClickListener {
            findNavController().navigate(
                POIEditFragmentDirections.actionPOIEditFragmentToLocationDialogFragment(
                    viewModel!!.longitude!!.toFloat(), viewModel!!.latitude!!.toFloat()
                )
            )
        }

        binding.buttonUpload.setOnClickListener {
            binding.uploadProgress.visibility = View.VISIBLE
            binding.buttonUpload.isEnabled = false
            binding.buttonCancel.isEnabled = false

            viewModel!!.uploadStatus.observe(viewLifecycleOwner) {
                binding.uploadProgress.visibility = View.GONE
                binding.buttonUpload.isEnabled = true
                binding.buttonCancel.isEnabled = true
                MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Material3)
                    .setMessage(it)
                    .setPositiveButton(R.string.dialog_ok, null)
                    .show()
            }

            val typeID = selectedType?.id ?: Configs.UNSORTED_ID
            viewModel!!.makeUploadRequest(
                POIArgs(
                    binding.titleInput.text.toString(),
                    typeID,
                    attachmentPath,
                    binding.descriptionInput.text.toString()
                )
            )
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadTypeSelector(typeSelector: AutoCompleteTextView?) {
        viewModel!!.getExistedPOITypes()
        viewModel!!.poiTypes.observe(viewLifecycleOwner) {
            val poiTypesAdapter = ArrayAdapter<POITypeDataPair>(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item
            )
            poiTypesAdapter.addAll(it)
            typeSelector?.setAdapter(poiTypesAdapter)
        }
        typeSelector?.setOnItemClickListener { adapterView, _, i, _ ->
            selectedType = adapterView.adapter.getItem(i) as POITypeDataPair?
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}