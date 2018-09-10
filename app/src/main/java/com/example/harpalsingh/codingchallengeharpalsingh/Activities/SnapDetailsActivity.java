package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.example.harpalsingh.codingchallengeharpalsingh.Fragments.SnapDetailFragment;
import com.example.harpalsingh.codingchallengeharpalsingh.Fragments.SnapGridFragment;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnapDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_detail);

        startGridFragment();
    }

    void startGridFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(R.id.details_content_frame, new SnapDetailFragment())
                .commit();
    }
}







