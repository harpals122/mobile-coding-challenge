package com.example.harpalsingh.codingchallengeharpalsingh.Models;

import java.util.ArrayList;

public class AllData {
    private static final AllData ourInstance = new AllData();

    public static AllData getInstance() {
        return ourInstance;
    }

    private AllData() {
    }

    private ArrayList<PhotoDatum> photoData = new ArrayList<>();

    public void setPhotoData(ArrayList<PhotoDatum> photoData) {
        this.photoData = photoData;
    }

    public ArrayList<PhotoDatum> getPhotoData() {
        return photoData;
    }

}