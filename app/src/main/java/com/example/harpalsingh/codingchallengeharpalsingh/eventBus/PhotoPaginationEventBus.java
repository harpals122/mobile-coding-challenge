package com.example.harpalsingh.codingchallengeharpalsingh.eventBus;

import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoData;

import java.util.ArrayList;

public class PhotoPaginationEventBus {
    private final int initial_count;
    private final ArrayList<PhotoData> photoData;

    public PhotoPaginationEventBus(ArrayList<PhotoData> photoData, int initial_count) {
        this.photoData = photoData;
        this.initial_count = initial_count;
    }

    public int getInitialCount() {
        return initial_count;
    }

    public ArrayList<PhotoData> getPhotoData() {
        return photoData;
    }
}