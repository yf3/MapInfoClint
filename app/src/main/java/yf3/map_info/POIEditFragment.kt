package yf3.map_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class POIEditFragment : Fragment() {

    private var attachmentPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            attachmentPath = POIEditFragmentArgs.fromBundle(it).attachmentPath
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
        val poiTypesAdapter: ArrayAdapter<CharSequence>? =
            context?.let { ArrayAdapter.createFromResource(it, R.array.poi_types, R.layout.support_simple_spinner_dropdown_item) }
        typeSelector?.setAdapter(poiTypesAdapter)
    }
}