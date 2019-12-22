package com.ozturktolunay.cultour;

import com.google.android.gms.maps.model.LatLng;

public class GPlace {

    private String photoReference;
    private String address;
    private String name;
    private String id;

    private Double rating;
    private Double latitude;
    private Double longitude;

    public GPlace(String photoReference, String address, String name, String id, Double rating,
                  Double latitude, Double longitude) {
        this.photoReference = photoReference;
        this.address = address;
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() { return latitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
