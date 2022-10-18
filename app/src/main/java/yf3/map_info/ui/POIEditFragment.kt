package yf3.map_info.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import yf3.map_info.*
import yf3.map_info.data.POITypeDataPair
import yf3.map_info.util.POIArgs

class POIEditFragment : Fragment() {

    private var viewModel: POIEditorViewModel? = null
    private var attachmentPath: String? = null
    private var selectedType: POITypeDataPair? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[POIEditorViewModel::class.java]
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
    ): View? {
        return inflater.inflate(R.layout.fragment_poi_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val progress: LinearProgressIndicator? = view.findViewById(R.id.uploadProgress)
        progress?.visibility = View.INVISIBLE
        val titleEditText: TextInputEditText? = view.findViewById(R.id.title_input)

        val typeSelector: MaterialAutoCompleteTextView? = view.findViewById(R.id.type_selector)
        setTypeSelector(typeSelector)

        val locationText: TextView = view.findViewById(R.id.location_text)
        locationText.text = viewModel?.let { String.format("(%.3f, %.3f)", it.longitude, it.latitude) }

        val locationIcon: ImageView? = view.findViewById(R.id.locationIcon)
        locationIcon?.setOnClickListener {
            findNavController().navigate(
                POIEditFragmentDirections.actionPOIEditFragmentToLocationDialogFragment(
                    viewModel!!.longitude.toFloat(), viewModel!!.latitude.toFloat()
                )
            )
        }

        val descriptionEditText: TextInputEditText? = view.findViewById(R.id.description_input)

        val submitBtn: Button = view.findViewById(R.id.button_upload)
        val returnBtn: Button = view.findViewById(R.id.button_cancel)

        submitBtn.setOnClickListener {
            progress?.visibility = View.VISIBLE
            submitBtn?.isEnabled = false
            returnBtn?.isEnabled = false

            viewModel!!.uploadStatus.observe(viewLifecycleOwner) {
                progress?.visibility = View.GONE
                submitBtn?.isEnabled = true
                returnBtn?.isEnabled = true
                MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Material3)
                    .setMessage(it)
                    .setPositiveButton(R.string.dialog_ok, null)
                    .show()
            }

            val typeID = selectedType?.id ?: Configs.UNSORTED_ID
            viewModel!!.upload(
                POIArgs(
                    titleEditText?.text.toString(),
                    typeID,
                    attachmentPath, descriptionEditText?.text.toString()
                )
            )
        }

        returnBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setTypeSelector(typeSelector: AutoCompleteTextView?) {
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
}