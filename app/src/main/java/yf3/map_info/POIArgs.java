package yf3.map_info;

public class POIArgs {
    public final String title;
    public final int typeID;
    public final String filePath;
    public final String comment;

    POIArgs(String title, int typeID, String filePath, String comment) {
        this.title = title;
        this.typeID = typeID;
        this.filePath = filePath;
        this.comment = comment;
    }
}
