package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.GridViewAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.GenericEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.GenericApiCalls.GenericApiCalls;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GridViewAdapter.ItemClick {


    private static Bundle bundle;
    private final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    private final String recyclerViewStateStateKey = "recyclerViewState";
    @BindView(R.id.app_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.gridRecyclerView)
    RecyclerView recyclerView;
    private Parcelable recyclerViewState;
    private GridViewAdapter gridAdapter;
    private ArrayList<PhotoDatum> photoData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundle != null) {
            Parcelable listState = bundle.getParcelable(recyclerViewStateStateKey);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bundle = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(recyclerViewStateStateKey, listState);
    }

    @Override
    public void itemClick(int currentPosition) {
        Intent intent = new Intent(MainActivity.this, SnapDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", currentPosition);
        bundle.putSerializable("photoData", photoData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFirstApiCall(GenericEventBus event) {
        this.photoData = AllData.getInstance().getPhotoData();
        gridAdapter = new GridViewAdapter(this, this.photoData, this);
        recyclerView.setAdapter(gridAdapter);
    }

    private void loadData() {
        this.photoData = AllData.getInstance().getPhotoData();

        if (this.photoData != null && this.photoData.size() > 0) {
            gridAdapter = new GridViewAdapter(this, this.photoData, this);
            recyclerView.setAdapter(gridAdapter);
        } else {
            GenericApiCalls genericApiCalls = new GenericApiCalls(this);
            genericApiCalls.doUnsplashRequest(2, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollDataLoad(PhotoPaginationEventBus event) {
        this.photoData.add(event.getPhotoData().get(3));
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.facebook:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}







