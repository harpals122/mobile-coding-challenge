package com.example.harpalsingh.codingchallengeharpalsingh.interfaces;

import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.harpalsingh.codingchallengeharpalsingh.constants.Constants.UNSPLASH_ENDPOINT;

public interface PhotosInterface {
    @GET(UNSPLASH_ENDPOINT)
    Call<List<PhotoData>> getPhotos(@Query("client_id") String client_id, @Query("page") int page_number, @Query("per_page") int per_page);
}