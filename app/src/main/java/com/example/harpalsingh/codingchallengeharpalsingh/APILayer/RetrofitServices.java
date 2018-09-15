package com.example.harpalsingh.codingchallengeharpalsingh.APILayer;

import com.example.harpalsingh.codingchallengeharpalsingh.interfaces.PhotosInterface;
import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoData;

import java.util.List;

import retrofit2.Call;

public class RetrofitServices {

    private static RetrofitServices SERVICE_INSTANCE = null;

    private RetrofitServices() {
    }

    public static RetrofitServices getNYServiceInstance() {
        if (SERVICE_INSTANCE == null) {
            SERVICE_INSTANCE = new RetrofitServices();
        }
        return SERVICE_INSTANCE;
    }

    public Call<List<PhotoData>> getPhotos(String client_id, int page_number, int per_page) {
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getPhotos(client_id, page_number, per_page);
    }
}