package yf3.map_info;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationParserTest {

    @Test
    public void getValueFromDescription() {
        try {
            assertEquals(121.4, LocationParser.getValueFromDescription("Longitude:121.4 Latitude:23.9", LocationParser.LONGITUDE_LABEL), 0.1);
        } catch (LocationParser.LabelNotFoundException e) {
            e.printStackTrace();
        }
    }
}