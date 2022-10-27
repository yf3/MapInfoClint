package yf3.map_info.data;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import yf3.map_info.util.POIArgs;
import yf3.map_info.util.PhotoModel;

public class PoiRequestWrapper {
    public final RequestBody mTitleStringBody;
    public final int mTypeID;
    public final MultipartBody.Part mImagePart;
    public final double mLongitude;
    public final double mLatitude;
    public final RequestBody mCommentStringBody;

    private static final String MEDIA_PARSE_TYPE = "image/*";
    private static final String MEDIA_FIELD_NAME = "attachment";

    public PoiRequestWrapper(POIArgs poiArgs) {
        mTitleStringBody = getStringBody(poiArgs.title);
        mTypeID = poiArgs.typeID;
        mImagePart = getMediaPart(poiArgs.filePath);
        LocationParser.LongLatPair longLatPair = PhotoModel.getLocationPair(poiArgs.filePath);
        mLongitude = longLatPair.longitude;
        mLatitude = longLatPair.latitude;
        mCommentStringBody = getStringBody(poiArgs.comment);
    }

    // Use Request Body instead of String because of the Retrofit converter adding quote problem
    private RequestBody getStringBody(String string) {
        return RequestBody.create(string, MediaType.parse("text/plain"));
    }

    private MultipartBody.Part getMediaPart(String filePath) {
        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse(MEDIA_PARSE_TYPE)); // as argument
        return MultipartBody.Part.createFormData(MEDIA_FIELD_NAME, file.getName(), fileReqBody);
    }
}
