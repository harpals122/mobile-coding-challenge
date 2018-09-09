package com.example.harpalsingh.codingchallengeharpalsingh.Models;

import java.util.ArrayList;

public class AllData {
    private static final AllData ourInstance = new AllData();
    private ArrayList<PhotoDatum> photoData = new ArrayList<>();

    private AllData() {
    }

    public static AllData getInstance() {
        return ourInstance;
    }

    public ArrayList<PhotoDatum> getPhotoData() {
        return photoData;
    }

    public void setPhotoData(ArrayList<PhotoDatum> photoArray) {
        this.photoData.addAll(photoArray);
    }

}