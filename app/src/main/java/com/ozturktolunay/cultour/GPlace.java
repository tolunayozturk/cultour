package com.ozturktolunay.cultour;

public class GPlace {

    private String photoReference;
    private String address;
    private String name;
    private String id;

    private Double rating;

    public GPlace(String photoReference, String address, String name, String id, Double rating) {
        this.photoReference = photoReference;
        this.address = address;
        this.name = name;
        this.id = id;
        this.rating = rating;
    }

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
