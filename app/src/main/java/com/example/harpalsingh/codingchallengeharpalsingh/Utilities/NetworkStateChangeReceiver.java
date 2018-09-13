package com.example.harpalsingh.codingchallengeharpalsingh.Utilities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.example.harpalsingh.codingchallengeharpalsingh.Interfaces.KeyConfig;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

    private View parentView;
    private static Snackbar snackbar = null;

    public NetworkStateChangeReceiver() {
    }

    public NetworkStateChangeReceiver(View parentView) {
        this.parentView = parentView;
        setupSnackBar();
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context ctx, final Intent intent) {

        Context context;
        context = ctx;

        String status = NetworkUtilityClass.getConnectivityStatusString(context);

        if (status.contentEquals("No connection")) {
            snackbar.show();
        } else {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
    }

    private void setupSnackBar() {
        snackbar = Snackbar.make(parentView, KeyConfig.noInternetConnectionMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View view = snackbar.getView();
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
    }
}