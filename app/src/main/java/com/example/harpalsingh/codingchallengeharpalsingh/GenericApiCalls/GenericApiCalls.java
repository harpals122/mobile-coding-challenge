package com.example.harpalsingh.codingchallengeharpalsingh.GenericApiCalls;

import android.content.Context;
import android.widget.Toast;

import com.example.harpalsingh.codingchallengeharpalsingh.APILayer.RetrofitServices;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.Interfaces.KeyConfig;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericApiCalls {

    private final Context context;
    private ArrayList<PhotoDatum> photoDatum = new ArrayList<>();

    public GenericApiCalls(Context context) {
        this.context = context;
    }

    public void doUnsplashRequest(int page_number, final int total_count) {
        Call<List<PhotoDatum>> callPlaces = RetrofitServices.getNYServiceInstance().getPhotos(KeyConfig.client_key, page_number,KeyConfig.per_page);
        callPlaces.enqueue(new Callback<List<PhotoDatum>>() {
            @Override
            public void onResponse(Call<List<PhotoDatum>> call, Response<List<PhotoDatum>> response) {
                if (response.code() == 200) {
                    photoDatum = (ArrayList<PhotoDatum>) response.body();
                    AllData.getInstance().setPhotoData(photoDatum);
                    EventBus.getDefault().post(new PhotoPaginationEventBus(photoDatum,total_count));
                } else {
                    Toast.makeText(context, "Make sure you have proper Unauthorization key to access unsplash API ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PhotoDatum>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong. Error message :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
