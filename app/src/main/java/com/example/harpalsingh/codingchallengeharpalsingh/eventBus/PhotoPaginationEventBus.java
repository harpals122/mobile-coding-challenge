package com.example.harpalsingh.codingchallengeharpalsingh.eventBus;

import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoDatum;

import java.util.ArrayList;

public class PhotoPaginationEventBus {
    private ArrayList<PhotoDatum> photoData;
    private int initial_count;

    public PhotoPaginationEventBus(ArrayList<PhotoDatum> photoData, int initial_count) {
        this.photoData = photoData;
        this.initial_count = initial_count;
    }

    public ArrayList<PhotoDatum> getPhotoData() {
        return photoData;
    }

    public int getInitialCount() {
        return initial_count;
    }
}