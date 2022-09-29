package csie.ndhu.mapInfo;

import android.location.Location;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PhotoModel {
    File mFile;
    public static final String PHOTO_EXT = ".jpeg";

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
}
