package yf3.map_info.data;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import yf3.map_info.util.POIArgs;
import yf3.map_info.util.PhotoModel;

public class PointOfInterest {
    public final RequestBody mTitleStringBody;
    public final int mTypeID;
    public final MultipartBody.Part mImagePart;
    public final double mLongitude;
    public final double mLatitude;
    public final RequestBody mCommentStringBody;

    public PointOfInterest(POIArgs poiArgs) {
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
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }

    private MultipartBody.Part getMediaPart(String filePath) {
        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file); // as argument
        // TODO: attachment as constant
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("attachment", file.getName(), fileReqBody);
        return filePart;
    }
}
