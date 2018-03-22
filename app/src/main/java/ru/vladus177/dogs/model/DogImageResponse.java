package ru.vladus177.dogs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DogImageResponse {

    @SerializedName("message")
    private List<String> imageList;

    public DogImageResponse(List<String> images) {
    }

    public List<String> getBreedImage() {
        return imageList;
    }

    public void setBreedsList(List<String> imageList) {
        this.imageList = imageList;
    }
}
