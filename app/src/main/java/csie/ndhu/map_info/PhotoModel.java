package csie.ndhu.map_info;

import android.location.Location;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoModel {
    public static final String PHOTO_EXT = ".jpeg";
    private File mFile;
    private ExifInterface mExifInterface;

    public PhotoModel(File file) {
        mFile = file;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public void writeLocationExif(Location location) {
        final ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(mFile.getAbsolutePath());
            String locationDescription = "Longitude:" + location.getLongitude() +
                    " " + "Latitude:" + location.getLatitude();
            exifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, locationDescription);
            exifInterface.saveAttributes();
            Log.i("Exif", "" + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION));
        } catch (FileNotFoundException e) {
            Log.d(null, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(null, "Error accessing file: " + e.getMessage());
        }
    }

    public String getLocationExif() {
        String locationDescription;
        try {
            final ExifInterface exifInterface = new ExifInterface(mFile.getAbsolutePath());
            locationDescription = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
        } catch (IOException e) {
            locationDescription = e.getMessage();
            e.printStackTrace();
        }
        return locationDescription;
    }
}
