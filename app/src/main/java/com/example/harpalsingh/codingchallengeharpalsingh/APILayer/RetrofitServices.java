package com.example.harpalsingh.codingchallengeharpalsingh.APILayer;

import android.content.Context;
import com.example.harpalsingh.codingchallengeharpalsingh.Interfaces.PhotosInterface;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoData;
import retrofit2.Call;

public class RetrofitServices {

    private static RetrofitServices nyServiceInstance = null;
    Context ctx;

    public RetrofitServices(Context context) {
        this.ctx = context;
    }

    public static RetrofitServices getNYServiceInstance(Context ctx) {
        if (nyServiceInstance == null) {
            nyServiceInstance = new RetrofitServices(ctx);
        }
        return nyServiceInstance;
    }

    public Call<PhotoData> getPhotos() {
        return RetrofitAPIClient.NYAPIClient(ctx).create(PhotosInterface.class).getPhotos();
    }
}

