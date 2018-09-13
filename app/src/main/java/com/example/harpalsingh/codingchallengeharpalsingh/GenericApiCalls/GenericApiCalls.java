package com.example.harpalsingh.codingchallengeharpalsingh.GenericApiCalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.harpalsingh.codingchallengeharpalsingh.APILayer.RetrofitServices;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.Interfaces.KeyConfig;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.Utilities.Utilities;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericApiCalls {

    private static ProgressDialog dialog;
    private static ArrayList<PhotoDatum> photoDatum = new ArrayList<>();

    public static void doUnsplashRequest(int page_number, final int initial_count,
                                         final View snackBarParentView, @Nullable final RelativeLayout mainLayout,
                                         @Nullable final RelativeLayout errorLayout, @Nullable Context context) {
        if (initial_count == 1) {
            dialog = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        }

        Call<List<PhotoDatum>> callPlaces = RetrofitServices.getNYServiceInstance().getPhotos(KeyConfig.client_key, page_number, KeyConfig.per_page);
        callPlaces.enqueue(new Callback<List<PhotoDatum>>() {
            @Override
            public void onResponse(Call<List<PhotoDatum>> call, Response<List<PhotoDatum>> response) {
                if (response.code() == 200) {
                    onFirstSuccessResponse(initial_count, mainLayout, errorLayout);

                    photoDatum = (ArrayList<PhotoDatum>) response.body();
                    AllData.getInstance().setPhotoData(photoDatum);
                    EventBus.getDefault().post(new PhotoPaginationEventBus(photoDatum, initial_count));
                } else {
                    Utilities.showSnackBar(KeyConfig.networkCallFailMessage, snackBarParentView);
                }
            }

            @Override
            public void onFailure(Call<List<PhotoDatum>> call, Throwable t) {
                if (initial_count == 1) {
                    onFirstFailure(initial_count, mainLayout, errorLayout);
                } else {
                    Utilities.showSnackBar(KeyConfig.networkCallFailMessage, snackBarParentView);
                }
            }
        });
    }

    private static void onFirstSuccessResponse(int initial_count, RelativeLayout mainLayout, RelativeLayout errorLayout) {
        if (initial_count == 1) {
            dialog.dismiss();
            errorLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    private static void onFirstFailure(int initial_count, RelativeLayout mainLayout, RelativeLayout errorLayout) {
        if (initial_count == 1) {
            dialog.dismiss();
            errorLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }
    }
}