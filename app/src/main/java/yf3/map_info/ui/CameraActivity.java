package yf3.map_info.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import yf3.map_info.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


public class CameraActivity extends AppCompatActivity {

    private CameraViewModel viewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private androidx.camera.core.Camera camera;
    private ProcessCameraProvider mCameraProvider;
    private ImageCapture imageCapture;
    private Button captureButton;

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
        viewModel.setupLocationFeature();
        final Observer<Location> locationObserver = location -> {
            onPhotoLocationFound();
        };
        viewModel.getObservedLocation().observe(this, locationObserver);
    }

    private final View.OnClickListener captureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            captureButton.setVisibility(View.INVISIBLE);
            Log.i("Camera", "Capture clicked.");
            viewModel.takePhoto(imageCapture);
        }
    };

    public void onPhotoLocationFound() {
        // Stop the finding location UI
        captureButton.setVisibility(View.VISIBLE);
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
