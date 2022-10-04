package csie.ndhu.map_info;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PointOfInterest {
    private String title;
    private File filePath;
    private double longitude;
    private double latitude;

    private RequestBody getStringBody(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }
}
