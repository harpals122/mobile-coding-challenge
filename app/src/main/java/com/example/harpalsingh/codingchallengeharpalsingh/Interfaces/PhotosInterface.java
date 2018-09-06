package com.example.harpalsingh.codingchallengeharpalsingh.Interfaces;

import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.harpalsingh.codingchallengeharpalsingh.Constants.Constants.UNSPLASH_ENDPOINT;


public interface PhotosInterface {

    @GET(UNSPLASH_ENDPOINT)
    Call<List<PhotoDatum>> getPhotos(@Query("client_id") String client_id,@Query("page")int page_number);
}
