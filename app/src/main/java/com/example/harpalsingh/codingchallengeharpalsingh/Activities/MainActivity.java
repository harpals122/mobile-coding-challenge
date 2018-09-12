package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.GridViewAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.GenericApiCalls.GenericApiCalls;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GridViewAdapter.ItemClick {

    @BindView(R.id.app_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private Bundle bundle = new Bundle();
    private final String recyclerViewStateStateKey = "recyclerViewState";
    private final Random random = new Random();
    private Handler handler;
    int requestCode = 100;

    @BindView(R.id.gridRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loading_progress_bar)
    ProgressBar progressBar;
    private GridViewAdapter gridAdapter;

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

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        gridAdapter = new GridViewAdapter(this, AllData.getInstance().getPhotoData(), MainActivity.this, recyclerView);
        recyclerView.setAdapter(gridAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToPadding(false);

        int Initial_count = 1;

        loadData(Initial_count);

        handler = new Handler();
        gridAdapter.setOnLoadMoreListener(new GridViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(final int totalCount) {
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(totalCount);
                        gridAdapter.setLoaded();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });

    }

    private void loadData(final int totalCount) {
        int randomNumber = random.nextInt(99 - 1) + 65;
        GenericApiCalls genericEventBus = new GenericApiCalls(this);
        genericEventBus.doUnsplashRequest(randomNumber, totalCount);
    }

    @Override
    public void itemClick(final int currentPosition, final ImageView imageView) {
        Intent intent = new Intent(this, SnapDetailsActivity.class);
        intent.putExtra("currentPosition", currentPosition);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bundle != null) {
            Parcelable listState = bundle.getParcelable(recyclerViewStateStateKey);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        if (SnapDetailsActivity.position != -1) {
            recyclerView.scrollToPosition(SnapDetailsActivity.position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bundle = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(recyclerViewStateStateKey, listState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhotoPaginationEventBus event) {
        gridAdapter.notifyItemRangeInserted(event.getTotal_count(), 10);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}







