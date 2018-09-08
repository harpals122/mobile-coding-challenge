package com.example.harpalsingh.codingchallengeharpalsingh.EventBus;

import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import java.util.ArrayList;
import java.util.List;

public class PhotoPaginationEventBus {
    private ArrayList<PhotoDatum> photoData = new ArrayList<>();

    public PhotoPaginationEventBus(ArrayList<PhotoDatum> photoData) {
        this.photoData = photoData;
    }

    public ArrayList<PhotoDatum> getPhotoData() {
        return photoData;
    }
}