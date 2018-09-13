package com.example.harpalsingh.codingchallengeharpalsingh.EventBus;

import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import java.util.ArrayList;

public class PhotoPaginationEventBus {
    private ArrayList<PhotoDatum> photoData;
    private int initialCount;

    public PhotoPaginationEventBus(ArrayList<PhotoDatum> photoData, int initial_count) {
        this.photoData = photoData;
        this.initialCount = initialCount;
    }

    public ArrayList<PhotoDatum> getPhotoData() {
        return photoData;
    }

    public int getInitialCount() {
        return initialCount;
    }
}