package com.example.harpalsingh.codingchallengeharpalsingh.EventBus;

import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import java.util.List;

public class GenericEventBus {
    private final List<PhotoDatum> photoData;

    public GenericEventBus(List<PhotoDatum> photoData) {
        this.photoData = photoData;
    }

    @SuppressWarnings("unused")
    public List<PhotoDatum> getPhotoData() {
        return photoData;
    }
}