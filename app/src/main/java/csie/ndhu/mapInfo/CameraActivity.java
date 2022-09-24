package csie.ndhu.mapInfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CameraActivity extends AppCompatActivity implements MapDialogFragment.OnFragmentInteractionListener {

    private CameraViewModel viewModel;

    private Camera mCamera;
    private CameraPreview mPreview;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private androidx.camera.core.Camera camera;
    private ImageCapture imageCapture;
    private Button captureButton;

    private Location foundLocation;
    final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);

        captureButton = findViewById(R.id.button_capture);
        captureButton.setOnClickListener(captureListener);

        requestPermissions();
    }

    private Preview getPreview() {
        Preview preview = new Preview.Builder().build();
        PreviewView previewView = findViewById(R.id.camera_preview_view);
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        return preview;
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()-> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
                // ImageCapture case
                imageCapture = new ImageCapture.Builder().build();
                // TODO: Consider adjusting rotation
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, getPreview());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void activatePhotoLocationFeature() {
        viewModel.setupLocationRepo();
        final Observer<Location> locationObserver = location -> {
            foundLocation = location;
            onPhotoLocationFound(location.getLatitude(), location.getLongitude());
        };
        viewModel.getInstantLocation().observe(this, locationObserver);
    }

    private View.OnClickListener captureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("Camera", "Capture clicked.");
            Executor takePictureExecutor = Executors.newSingleThreadExecutor();
            imageCapture.takePicture(takePictureExecutor, new ImageCapture.OnImageCapturedCallback() {
                @Override
                public void onCaptureSuccess(@NonNull ImageProxy imageProxy) {
                    super.onCaptureSuccess(imageProxy);
                    // TODO: Consider adjusting rotation
                    viewModel.updateCurrentLocation();
                    // Show finding location UI
                }
            });
        }
    };

    public void onPhotoLocationFound(double latitude, double longitude) {
        MapDialogFragment dialogFragment = MapDialogFragment.newInstance(latitude, longitude);
        dialogFragment.show(getSupportFragmentManager(), "map");
    }

    private File getImageFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CheckIn"); // TODO: avoid hard-coded

        if (! mediaStorageDir.exists()) {
            if (! mediaStorageDir.mkdirs()) {
                Log.d("CheckIn", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
        return mediaFile;
    }

    private boolean hasPermissions() {
        Context context = this.getApplicationContext();
        for (String PERMISSION: PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityResultLauncher<String[]> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted
                        -> {
                    if (isGranted.get(Manifest.permission.CAMERA)) {
                        startCamera();
                    }
                    if (isGranted.get(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        isGranted.get(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        activatePhotoLocationFeature();
                    }
                });
        requestPermissionLauncher.launch(PERMISSIONS);
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        public static final int MEDIA_TYPE_IMAGE = 1;
        public static final int MEDIA_TYPE_VIDEO = 2;

        private Uri getOutputMediaFileUri(int type){
            return Uri.fromFile(getOutputMediaFile(type));
        }

        private File getOutputMediaFile(int type) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "CheckIn");

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()) {
                if (! mediaStorageDir.mkdirs()) {
                    Log.d("CheckIn", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_"+ timeStamp + ".jpg");
            } else if(type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_"+ timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(null, "Error creating media file, check storage permissions");
                return;
            }

            final ExifInterface exifInterface;
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);

                exifInterface = new ExifInterface(pictureFile.getAbsolutePath());
                exifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, "Longitude:" + foundLocation.getLongitude() + " " + "Latitude:" + foundLocation.getLatitude());
                exifInterface.saveAttributes();
                Log.i("exif", "" + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION));
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(null, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(null, "Error accessing file: " + e.getMessage());
            }
            mCamera.startPreview();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private void doTakePicture() {
        mCamera.takePicture(null, null, mPicture);
    }

    @Override
    public void onFragmentInteraction() {
        Log.i("FragmentInteraction", "It Worked!");
//        doTakePicture();
    }
}
