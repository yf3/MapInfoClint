package csie.ndhu.map_info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class GalleryDialogFragment extends DialogFragment {
    private static final String ARG_PARAM = "param";

    private POIEditorViewModel mViewModel;

    private String photoPath;

    private ImageView imageView;
    private Button buttonUpload;
    private TextView textViewResult;
    private TextInputEditText titleEditText;
    private TextInputEditText commentEditText;
    private ImageButton checkLocationButton;

    public GalleryDialogFragment() {
        // Required empty public constructor
    }

    public static GalleryDialogFragment newInstance(String param) {
        GalleryDialogFragment fragment = new GalleryDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        Log.i("Dialog Factory", args.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(POIEditorViewModel.class);
        photoPath = GalleryDialogFragmentArgs.fromBundle(getArguments()).getFileName();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_gallery_dialog, null);
        builder.setView(view);

        titleEditText = view.findViewById(R.id.title_text);
        commentEditText = view.findViewById(R.id.photo_comment_text);
        imageView = view.findViewById(R.id.imageViewTemp);
        imageView.setImageDrawable(Drawable.createFromPath(photoPath));

        textViewResult = view.findViewById(R.id.text_result);
        final Observer<String> uploadObserver = uploadStatus -> {
            textViewResult.setText(uploadStatus);
        };
        mViewModel.getUploadStatus().observe(this, uploadObserver);

        // TODO: check photo location
        checkLocationButton = view.findViewById(R.id.imageButton);
        checkLocationButton.setOnClickListener(lambdaView -> {
            LocationParser.LongLatPair longLatPair = PhotoModel.getLocationPair(photoPath);
            MapDialogFragment mapDialogFragment = MapDialogFragment.newInstance(longLatPair.getLatitude(), longLatPair.getLongitude());
            mapDialogFragment.show(getChildFragmentManager(), "map");
        });

        buttonUpload = view.findViewById(R.id.button_upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POIArgs poiArgs = new POIArgs(titleEditText.getText().toString(), photoPath, commentEditText.getText().toString());
                mViewModel.upload(poiArgs);
            }
        });

        Spinner poiTypeSpinner = view.findViewById(R.id.poitype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.poi_types, android.R.layout.simple_spinner_item); // dropdown layout?
        poiTypeSpinner.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
