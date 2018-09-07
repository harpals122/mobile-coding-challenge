package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.ViewPagerAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnapDetailsActivity extends AppCompatActivity {

    @BindView(R.id.app_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_details);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        @SuppressWarnings("unchecked")
        ArrayList<PhotoDatum> photoData = (ArrayList<PhotoDatum>) Objects.requireNonNull(bundle).getSerializable("photoData");
        int id = bundle.getInt("id");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, AllData.getInstance().getPhotoData());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


