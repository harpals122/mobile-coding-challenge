package com.example.harpalsingh.codingchallengeharpalsingh.APILayer;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIClient {

    public static Retrofit retrofit = null;
    private static String link = "google.com";

    public static Retrofit NYAPIClient(Context ctx) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit;
    }
}