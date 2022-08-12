package csie.ndhu.checkin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends AppCompatActivity implements LocationListener, MapDialogFragment.OnFragmentInteractionListener {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Button captureButton;

    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        mCamera = getCameraInstance();

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        captureButton = findViewById(R.id.button_capture);
        captureButton.setOnClickListener(captureListener);

        locationManager = new LocationManager();
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
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
                exifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, "Longitude:" + locationManager.getLongitude() + " " + "Latitude:" + locationManager.getLatitude());
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

    private View.OnClickListener captureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            locationManager.initialize(CameraActivity.this);
            locationManager.registerListener(CameraActivity.this);
            locationManager.updateAndNotify();
        }
    };

    @Override
    public void onLocationFound() {
        MapDialogFragment dialogFragment = MapDialogFragment.newInstance(locationManager.getLatitude(), locationManager.getLongitude());
        dialogFragment.show(getSupportFragmentManager(), "map");
    }

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
        doTakePicture();
    }
}
