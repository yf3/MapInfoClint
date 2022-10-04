package csie.ndhu.map_info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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
        photoPath = GalleryDialogFragmentArgs.fromBundle(getArguments()).getFileName();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_gallery_dialog, null);
        builder.setView(view);

        imageView = view.findViewById(R.id.imageViewTemp);
        imageView.setImageDrawable(Drawable.createFromPath(photoPath));
        textViewResult = view.findViewById(R.id.text_result);
        textViewResult.setText(""); // TODO: decouple
        titleEditText = view.findViewById(R.id.title_text);
        commentEditText = view.findViewById(R.id.photo_comment_text);
        // TODO: check photo location
        checkLocationButton = view.findViewById(R.id.imageButton);
        checkLocationButton.setOnClickListener(lambdaView -> {
            LocationParser.LongLatPair longLatPair = getLocationFromFile(new File(photoPath));
            MapDialogFragment mapDialogFragment = MapDialogFragment.newInstance(longLatPair.getLatitude(), longLatPair.getLongitude());
            mapDialogFragment.show(getChildFragmentManager(), "map");
        });

        buttonUpload = view.findViewById(R.id.button_upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: decouple
                textViewResult.setText("uploading");
                uploadToServer(photoPath);
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

    // Use Request Body instead of String because of the Retrofit converter adding quote problem
    private RequestBody getStringBody(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }

    private void uploadToServer(String filePath) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        File photo = new File(filePath);
        RequestBody imageReqBody = RequestBody.create(MediaType.parse("image/*"), photo);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("attachment", photo.getName(), imageReqBody);

        LocationParser.LongLatPair longLatPair = getLocationFromFile(photo);

        Log.i("Photo Location", String.format("%f, %f", longLatPair.getLongitude(), longLatPair.getLatitude()));

        Call call = uploadAPIs.uploadImage(getStringBody(titleEditText.getText().toString()),
                filePart,
                longLatPair.getLongitude(),
                longLatPair.getLatitude(),
                getStringBody(commentEditText.getText().toString()));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 400) {
                    try {
                        Log.i("Bad Request 400", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textViewResult.setText("Upload Failed!");
                }
                else {
                    Log.i("Response code", String.valueOf(response.code()));
                    textViewResult.setText("Upload Success!");
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
