package yf3.map_info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class GalleryDialogFragment extends DialogFragment {

    private POIEditorViewModel mViewModel;

    private String photoPath;

    private TextView textViewResult;

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

        textViewResult = view.findViewById(R.id.text_result);
        final Observer<String> uploadObserver = uploadStatus -> textViewResult.setText(uploadStatus);
        mViewModel.getUploadStatus().observe(this, uploadObserver);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
