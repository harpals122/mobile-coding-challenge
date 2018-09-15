package com.example.harpalsingh.codingchallengeharpalsingh.utilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.harpalsingh.codingchallengeharpalsingh.activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.genericApiCalls.GenericApiCalls;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.Objects;
import java.util.Random;

public class Utilities {

    public static void setupToolbarAndNavigationBar(final MainActivity mainActivity, Toolbar toolbar, NavigationView navigationView, final DrawerLayout drawerLayout) {
        mainActivity.setSupportActionBar(toolbar);
        ActionBar actionbar = mainActivity.getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public static void registerNetworkStateChangerReciever(MainActivity mainActivity, BroadcastReceiver networkBroadcast) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mainActivity.registerReceiver(networkBroadcast, filter);
    }

    public static void doNetworkRequest(final int initial_count, View snackBarParentView, @Nullable RelativeLayout mainlayout,
                                        @Nullable LinearLayout errorLayout, @Nullable Context context) {
        Random random = new Random();
        int randomNumber = random.nextInt(65 - 1) + 65;
        GenericApiCalls.doUnsplashRequest(randomNumber, initial_count, snackBarParentView, mainlayout, errorLayout, context);
    }

    public static void showSnackBar(String message, View snackBarParentView) {
        Snackbar snackbar = Snackbar.make(snackBarParentView, message, Snackbar.LENGTH_INDEFINITE).setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    public static View setUpViewWidth(View view, Activity activity) {

        ViewGroup.LayoutParams params = view.getLayoutParams();

        int orientation = activity.getResources().getConfiguration().orientation;
        int viewWidth;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);


        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewWidth = (int) (metrics.heightPixels * 0.9);
        } else {
            viewWidth = (int) (metrics.widthPixels * 0.9);
        }

        params.width = viewWidth;
        view.setPadding(15, 15, 15, 15);
        view.setLayoutParams(params);
        return view;
    }
}