package com.example.harpalsingh.codingchallengeharpalsingh.genericApiCalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.harpalsingh.codingchallengeharpalsingh.APILayer.RetrofitServices;
import com.example.harpalsingh.codingchallengeharpalsingh.eventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.interfaces.KeyConfig;
import com.example.harpalsingh.codingchallengeharpalsingh.models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoData;
import com.example.harpalsingh.codingchallengeharpalsingh.utilities.Utilities;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericApiCalls {

    private static ProgressDialog DIALOG;
    private static ArrayList<PhotoData> PHOTO_DATA = new ArrayList<>();

    public static void doUnsplashRequest(int page_number, final int initial_count,
                                         final View snackBarParentView, @Nullable final RelativeLayout mainLayout,
                                         @Nullable final LinearLayout errorLayout, @Nullable Context context) {

        if (initial_count == 1)
            DIALOG = ProgressDialog.show(context, "", "Loading. Please wait...", true);

        Call<List<PhotoData>> callPlaces = RetrofitServices.getNYServiceInstance().getPhotos(KeyConfig.client_key, page_number, KeyConfig.dataPerPage);
        callPlaces.enqueue(new Callback<List<PhotoData>>() {
            @Override
            public void onResponse(@NonNull Call<List<PhotoData>> call, @NonNull Response<List<PhotoData>> response) {
                if (response.code() == 200) {
                    onFirstSuccessResponse(initial_count, mainLayout, errorLayout);

                    PHOTO_DATA = (ArrayList<PhotoData>) response.body();
                    AllData.getInstance().setPhotoData(PHOTO_DATA);
                    EventBus.getDefault().post(new PhotoPaginationEventBus(PHOTO_DATA, initial_count));
                } else {
                    Utilities.showSnackBar(KeyConfig.networkCallFailMessage, snackBarParentView);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PhotoData>> call, @NonNull Throwable t) {
                if (initial_count == 1) {
                    onFirstFailure(initial_count, mainLayout, errorLayout);
                } else {
                    Utilities.showSnackBar(KeyConfig.networkCallFailMessage, snackBarParentView);
                }
            }
        });
    }

    private static void onFirstSuccessResponse(int initial_count, RelativeLayout mainLayout, LinearLayout errorLayout) {
        if (initial_count == 1) {
            DIALOG.dismiss();
            errorLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    private static void onFirstFailure(int initial_count, RelativeLayout mainLayout, LinearLayout errorLayout) {
        if (initial_count == 1) {
            DIALOG.dismiss();
            errorLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }
    }
}