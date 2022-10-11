package yf3.map_info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class POIEditFragment : Fragment() {

    private var viewModel: POIEditorViewModel? = null
    private var attachmentPath: String? = null

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
        val typeSelector: AutoCompleteTextView? = view.findViewById(R.id.type_selector)
        setTypeSelector(typeSelector)

        val locationText: TextView = view.findViewById(R.id.location_text)
        locationText.text = viewModel?.let { String.format("(%.3f, %.3f)", it.longitude, it.latitude) }

        val submitBtn: Button = view.findViewById(R.id.button_upload)

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
    }
}