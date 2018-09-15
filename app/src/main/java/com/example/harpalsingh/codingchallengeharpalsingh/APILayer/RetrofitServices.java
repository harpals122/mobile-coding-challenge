package com.example.harpalsingh.codingchallengeharpalsingh.APILayer;

import android.annotation.SuppressLint;

import com.example.harpalsingh.codingchallengeharpalsingh.interfaces.PhotosInterface;
import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoDatum;

import java.util.List;

import retrofit2.Call;

public class RetrofitServices {

    @SuppressLint("StaticFieldLeak")
    private static RetrofitServices SERVICE_INSTANCE = null;

    private RetrofitServices() {
    }

    public static RetrofitServices getNYServiceInstance() {
        if (SERVICE_INSTANCE == null) {
            SERVICE_INSTANCE = new RetrofitServices();
        }
        return SERVICE_INSTANCE;
    }

    public Call<List<PhotoDatum>> getPhotos(String client_id, int page_number, int per_page) {
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getPhotos(client_id, page_number,per_page);
    }
}