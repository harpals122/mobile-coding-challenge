package com.example.harpalsingh.codingchallengeharpalsingh.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Toast;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SnapDetailsActivityTest {

    @Rule
    public final ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class, true, false);

    @Test
    public void onSplashInternetFailureAssessmentTest() {
        Intent intent = new Intent();
        activityTestRule.launchActivity(null);

        final Activity activity = activityTestRule.getActivity();
        WifiManager wifiManager = null;

        if (activity != null)
            wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);

        assert wifiManager != null;
        wifiManager.setWifiEnabled(false);

        boolean mobileDataAllowed = Settings.Secure.getInt(activity.getContentResolver(), "mobile_data", 1) == 1;

        if (!mobileDataAllowed) {

            activityTestRule.launchActivity(intent);
            TestUtilities.onConnectionBackRetry(wifiManager);

        } else {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity.getApplicationContext(), "Please turn off Mobile data to test this functionality ", Toast.LENGTH_SHORT).show();
                }
            });

            TestUtilities.sleep(3000);
        }
    }

}