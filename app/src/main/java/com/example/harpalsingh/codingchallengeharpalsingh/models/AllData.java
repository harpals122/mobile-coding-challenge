package com.example.harpalsingh.codingchallengeharpalsingh.models;

import java.util.ArrayList;

public class AllData {
    private static final AllData ourInstance = new AllData();
    private final ArrayList<PhotoData> photoData = new ArrayList<>();

    private AllData() { }

    public static AllData getInstance() {
        return ourInstance;
    }

    public ArrayList<PhotoData> getPhotoData() {
        return photoData;
    }

    public void setPhotoData(ArrayList<PhotoData> photoArray) {
        this.photoData.addAll(photoArray);
    }
}