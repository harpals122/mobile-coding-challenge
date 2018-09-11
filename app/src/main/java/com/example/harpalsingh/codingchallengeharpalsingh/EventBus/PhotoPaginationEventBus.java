package com.example.harpalsingh.codingchallengeharpalsingh.EventBus;

import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import java.util.ArrayList;

public class PhotoPaginationEventBus {
    private ArrayList<PhotoDatum> photoData;
    private int total_count;

    public PhotoPaginationEventBus(ArrayList<PhotoDatum> photoData, int total_count) {
        this.photoData = photoData;
        this.total_count = total_count;
    }

    public ArrayList<PhotoDatum> getPhotoData() {
        return photoData;
    }

    public int getTotal_count() {
        return total_count;
    }
}