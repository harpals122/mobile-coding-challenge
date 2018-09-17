package com.example.harpalsingh.codingchallengeharpalsingh.activities;

import android.net.wifi.WifiManager;
import android.support.annotation.IdRes;
import android.support.test.espresso.ViewInteraction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.harpalsingh.codingchallengeharpalsingh.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class TestUtilities {

    public static void onConnectionBackRetry(WifiManager wifiManager) {

        sleep(2000);

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.snackbar_action), withText("Dismiss"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.Snackbar$SnackbarLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);

        sleep(3000);

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.retry), withText("Retry"),
                        childAtPosition(
                                allOf(withId(R.id.retry_parent_view),
                                        childAtPosition(
                                                withId(R.id.main_layout),
                                                1)),
                                3),
                        isDisplayed()));

        appCompatButton2.perform(click());
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static int getRecyclerViewAdapterDataSize(@IdRes int RecyclerViewId) {
        final int[] size = {0};
        Matcher matcher = new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                size[0] = ((RecyclerView) item).getAdapter().getItemCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
        //noinspection unchecked
        onView(allOf(withId(RecyclerViewId), isDisplayed())).check(matches(matcher));
        int result = size[0];
        size[0] = 0;
        return result;
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
