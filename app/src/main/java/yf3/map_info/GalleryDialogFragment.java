package yf3.map_info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

    private POIEditorViewModel mViewModel;

    private String photoPath;

    private TextView textViewResult;
    private TextInputEditText commentEditText;

    public GalleryDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(POIEditorViewModel.class);
        photoPath = GalleryDialogFragmentArgs.fromBundle(getArguments()).getFileName();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_gallery_dialog, (ViewGroup) getView());
        builder.setView(view);

        commentEditText = view.findViewById(R.id.photo_comment_text);
        ImageView imageView = view.findViewById(R.id.imageViewTemp);
        imageView.setImageDrawable(Drawable.createFromPath(photoPath));

        textViewResult = view.findViewById(R.id.text_result);
        final Observer<String> uploadObserver = uploadStatus -> textViewResult.setText(uploadStatus);
        mViewModel.getUploadStatus().observe(this, uploadObserver);

        ImageButton checkLocationButton = view.findViewById(R.id.imageButton);
//        checkLocationButton.setOnClickListener(lambdaView -> {
//            LocationParser.LongLatPair longLatPair = PhotoModel.getLocationPair(photoPath);
//            MapDialogFragment mapDialogFragment = MapDialogFragment.newInstance(longLatPair.getLatitude(), longLatPair.getLongitude());
//            mapDialogFragment.show(getChildFragmentManager(), "map");
//        });

        Button buttonUpload = view.findViewById(R.id.button_upload);
        buttonUpload.setOnClickListener(lambdaView -> {
            POIArgs poiArgs = new POIArgs("title", photoPath, commentEditText.getText().toString());
            mViewModel.upload(poiArgs);
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
