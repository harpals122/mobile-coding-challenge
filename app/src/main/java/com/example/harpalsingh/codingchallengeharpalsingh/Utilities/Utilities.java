package com.example.harpalsingh.codingchallengeharpalsingh.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.harpalsingh.codingchallengeharpalsingh.Activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.GenericApiCalls.GenericApiCalls;
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
                                        @Nullable RelativeLayout errorLayout, @Nullable Context context) {
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
}