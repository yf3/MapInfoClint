package csie.ndhu.mapInfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
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


public class CameraActivity extends AppCompatActivity {

    private CameraViewModel viewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private androidx.camera.core.Camera camera;
    private ProcessCameraProvider mCameraProvider;
    private ImageCapture imageCapture;
    private Button captureButton;

    private boolean hasLocationPermissions;
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

        hasLocationPermissions = false;
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
                mCameraProvider = cameraProviderFuture.get();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
                // ImageCapture case
                imageCapture = new ImageCapture.Builder().build();
                // TODO: Consider adjusting rotation
                camera = mCameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, getPreview());
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
            onPhotoLocationFound();
        };
        viewModel.getInstantLocation().observe(this, locationObserver);
    }

    private File getInternalImageFile() {
        File mediaStorageDir = getFilesDir();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpeg");
        return mediaFile;
    }

    private final View.OnClickListener captureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("Camera", "Capture clicked.");
            Executor takePictureExecutor = Executors.newSingleThreadExecutor();
            File imageFile = getInternalImageFile();
            ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(imageFile).build();
            imageCapture.takePicture(outputFileOptions, takePictureExecutor, new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    // TODO: Consider adjusting rotation
                    if (hasLocationPermissions) {
                        // Show finding location UI
                        viewModel.setPhotoToAddLocation(imageFile);
                        viewModel.requestCurrentLocation();
                    }
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {

                }
            });
        }
    };

    public void onPhotoLocationFound() {
        // Hide finding location UI
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
                        hasLocationPermissions = true;
                    }
                });
        requestPermissionLauncher.launch(PERMISSIONS);
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onPause() {
        if (null != mCameraProvider)
            mCameraProvider.unbindAll();
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (null != mCameraProvider)
            mCameraProvider.unbindAll();
        super.onStop();
    }
}
