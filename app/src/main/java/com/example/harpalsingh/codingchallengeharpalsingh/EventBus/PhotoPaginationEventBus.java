package com.example.harpalsingh.codingchallengeharpalsingh.EventBus;

import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import java.util.List;

public class PhotoPaginationEventBus {
    private final List<PhotoDatum> photoData;

    public PhotoPaginationEventBus(List<PhotoDatum> photoData) {
        this.photoData = photoData;
    }

    public List<PhotoDatum> getPhotoData() {
        return photoData;
    }
}