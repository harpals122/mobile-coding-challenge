package com.example.harpalsingh.codingchallengeharpalsingh.Interfaces;

import android.provider.SyncStateContract;

import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PhotosInterface {

    @FormUrlEncoded
    @POST()
    Call<PhotoData> getPhotos();
}
