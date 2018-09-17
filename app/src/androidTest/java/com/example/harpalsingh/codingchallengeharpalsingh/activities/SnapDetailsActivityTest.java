package com.example.harpalsingh.codingchallengeharpalsingh.activities;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.net.wifi.WifiManager;
        import android.support.test.InstrumentationRegistry;
        import android.support.test.espresso.contrib.RecyclerViewActions;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.test.suitebuilder.annotation.LargeTest;

        import com.example.harpalsingh.codingchallengeharpalsingh.R;
        import com.example.harpalsingh.codingchallengeharpalsingh.interfaces.KeyConfig;

        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.Espresso.pressBack;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;
        import static org.hamcrest.Matchers.allOf;
        import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SnapDetailsActivityTest {

    @Rule
    public final ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class, true, false);

    @Test
    public void onDetailRecyclerViewLazyLoadingAndPositionRestorationOnBackTest() {
        Intent intent = new Intent();
        activityTestRule.launchActivity(intent);

        final Activity activity = activityTestRule.getActivity();
        WifiManager wifiManager = null;

        if (activity != null)
            wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);

        assert wifiManager != null;
        if (!wifiManager.isWifiEnabled())
        {
            TestUtilities.sleep(1000);
            TestUtilities.onConnectionBackRetry(wifiManager);
        }


        TestUtilities.sleep(3000);

        Context context = InstrumentationRegistry.getTargetContext();

        onView(withId(R.id.grid_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        lazyLoadingTestMeathod();

        pressBack();

        TestUtilities.sleep(3000);
    }

    private void lazyLoadingTestMeathod() {

        for (int page_count = 0; page_count <= KeyConfig.testNumberOfPages; page_count++) {

            TestUtilities.sleep(3000);

            int itemCount = TestUtilities.getRecyclerViewAdapterDataSize(R.id.grid_details_recycler_viewpager);

            onView(withId(R.id.grid_details_recycler_viewpager)).perform(RecyclerViewActions.scrollToPosition(itemCount -1));
        }
    }
}