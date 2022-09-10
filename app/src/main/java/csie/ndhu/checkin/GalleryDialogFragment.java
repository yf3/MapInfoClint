package csie.ndhu.checkin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GalleryDialogFragment extends DialogFragment {
    private static final String ARG_PARAM = "param";
    private String photoPath;

    private ImageView imageView;
    private Button buttonUpload;
    private TextView textViewTest;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private TextInputLayout textInputLayout;

    private OnFragmentInteractionListener mListener;

    public GalleryDialogFragment() {
        // Required empty public constructor
    }

    public static GalleryDialogFragment newInstance(String param) {
        GalleryDialogFragment fragment = new GalleryDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoPath = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_gallery_dialog, null);
        builder.setView(view);

        imageView = view.findViewById(R.id.imageViewTemp);
        imageView.setImageDrawable(Drawable.createFromPath(photoPath));
        textViewTest = view.findViewById(R.id.textView2);
        textViewTest.setText("");

        buttonUpload = view.findViewById(R.id.button_upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTest.setText("uploading");
                uploadToServer(photoPath);
            }
        });

        checkBox1 = view.findViewById(R.id.checkBox1);
        checkBox2 = view.findViewById(R.id.checkBox2);

        textInputLayout = view.findViewById(R.id.photo_comment);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    // Do not use
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_dialog, container, false);
        return view;
    }

    @Override
    // Do not use
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    private void uploadToServer(String filePath) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        File photo = new File(filePath);
        RequestBody imageReqBody = RequestBody.create(MediaType.parse("image/*"), photo);
        MultipartBody.Part part = MultipartBody.Part.createFormData("attachment", photo.getName(), imageReqBody);

        LocationParser.LongLatPair longLatPair = getLocationFromFile(photo);

        Log.i("Photo Location", String.format("%f, %f", longLatPair.getLongitude(), longLatPair.getLatitude()));

        Call call = uploadAPIs.uploadImage(part,
                longLatPair.getLongitude(),
                longLatPair.getLatitude(),
                textInputLayout.getEditText().getText().toString());

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 400) {
                    try {
                        Log.i("Bad Request 400", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textViewTest.setText("Upload Failed!");
                }
                else {
                    Log.i("Response code", String.valueOf(response.code()));
                    textViewTest.setText("Upload Success!");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }

    private LocationParser.LongLatPair getLocationFromFile(File file) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(file.getAbsolutePath());
            String locationDescription = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
            return LocationParser.getLongLatPair(locationDescription, LocationParser.LONGITUDE_LABEL, LocationParser.LATITUDE_LABEL);
        } catch (IOException e) {
            e.printStackTrace();
            return new LocationParser.LongLatPair(0, 0);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
