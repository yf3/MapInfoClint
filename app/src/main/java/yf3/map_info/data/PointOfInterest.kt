package yf3.map_info.data

import com.google.gson.annotations.SerializedName

data class PointOfInterest (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val title: String,
    @SerializedName("attachment") val photoRelativePath: String,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("comment") val description: String,
    @SerializedName("poi_type") val poiType: Int
)