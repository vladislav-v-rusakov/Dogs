package ru.vladus177.dogs.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BreedsResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private Map<String, List<String>> message;

    public BreedsResponse(String success, Map<String, List<String>> message) {
    }

    public List<String> getBreedList() {
        return new ArrayList<>(message.keySet());
    }


    public void setMessage(Map<String, List<String>> message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
}
