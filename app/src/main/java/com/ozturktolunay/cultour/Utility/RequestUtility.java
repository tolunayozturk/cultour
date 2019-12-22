package com.ozturktolunay.cultour.Utility;

public class RequestUtility {

    public static String getRequestUrl(Double latitude, Double longitude, String placeType, String placesApiKey) {

        return String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s,%s&radius=25000&type=%s&key=%s", latitude, longitude, placeType, placesApiKey);
    }
}
