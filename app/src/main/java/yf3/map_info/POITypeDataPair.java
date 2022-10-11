package yf3.map_info;

import com.google.gson.annotations.SerializedName;

public class POITypeDataPair {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String typeName;

    public POITypeDataPair(int id, String name) {
        this.id = id;
        this.typeName = name;
    }

    @Override
    // ArrayAdapter use this to get the string of the dropdown menu option
    public String toString() {
        return typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
