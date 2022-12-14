package yf3.map_info.util;

import java.util.Scanner;

public class LocationParser {
    public static final String LONGITUDE_LABEL = "Longitude:";
    public static final String LATITUDE_LABEL = "Latitude:";

    public static double getValueFromDescription(String description, String labelName) throws LabelNotFoundException {
        if (labelName.equals(LONGITUDE_LABEL) || labelName.equals(LATITUDE_LABEL)) {
            int parsingFront = description.indexOf(labelName);
            parsingFront += labelName.length();
            String subString = description.substring(parsingFront);
            Scanner scanner = new Scanner(subString);
            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            }
            else {
                throw new LabelNotFoundException("Value not found");
            }
        }
        else {
            throw new LabelNotFoundException(labelName);
        }
    }

    public static class LongLatPair {
        public final double longitude;
        public final double latitude;
        public LongLatPair(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    public static LongLatPair getLongLatPair(String description) {
        double longitude, latitude;
        try {
            longitude = getValueFromDescription(description, LONGITUDE_LABEL);
            latitude = getValueFromDescription(description, LATITUDE_LABEL);
            return new LongLatPair(longitude, latitude);
        } catch (LabelNotFoundException e) {
            e.printStackTrace();
            return new LongLatPair(0, 0);
        }
    }

    public static class LabelNotFoundException extends Exception {
        public LabelNotFoundException(String wrongLabelName) {
            super("Label Error - " + wrongLabelName);
        }
    }
}
