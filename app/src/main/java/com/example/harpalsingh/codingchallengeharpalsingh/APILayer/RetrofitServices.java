package com.example.harpalsingh.codingchallengeharpalsingh.APILayer;

import android.annotation.SuppressLint;

import com.example.harpalsingh.codingchallengeharpalsingh.Interfaces.PhotosInterface;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import java.util.List;

import retrofit2.Call;


public class RetrofitServices {

    @SuppressLint("StaticFieldLeak")
    private static RetrofitServices nyServiceInstance = null;

    private RetrofitServices() {
    }

    public static RetrofitServices getNYServiceInstance() {
        if (nyServiceInstance == null) {
            nyServiceInstance = new RetrofitServices();
        }
        return nyServiceInstance;
    }

    public Call<List<PhotoDatum>> getPhotos(String client_id, int page_number, int per_page) {
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getPhotos(client_id, page_number,per_page);
    }
}

