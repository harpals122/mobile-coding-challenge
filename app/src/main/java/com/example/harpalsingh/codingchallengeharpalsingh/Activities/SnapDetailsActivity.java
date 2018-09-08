package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.ViewPagerAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnapDetailsActivity extends AppCompatActivity {

    @BindView(R.id.app_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_details);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        int id = bundle.getInt("id");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, AllData.getInstance().getPhotoData());



        viewPager.setClipToPadding(false);
        viewPager.setPadding(70, 0, 70, 0);
        viewPager.setPageMargin(20);

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


