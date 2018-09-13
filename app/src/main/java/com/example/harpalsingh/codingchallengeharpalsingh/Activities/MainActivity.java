package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.GridViewAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.R;
import com.example.harpalsingh.codingchallengeharpalsingh.Utilities.NetworkStateChangeReceiver;
import com.example.harpalsingh.codingchallengeharpalsingh.Utilities.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements GridViewAdapter.ItemClick {

    @BindView(R.id.app_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.gridRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loading_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.mainLayout)
    View parentView;
    @BindView(R.id.mainContentcontainer)
    RelativeLayout mainContentLayout;
    @BindView(R.id.retryParentView)
    RelativeLayout errorLayout;
    @BindView(R.id.retry)
    Button retry;

    private Bundle bundle = new Bundle();
    private final Handler handler = new Handler();
    private BroadcastReceiver networkBroadcast;
    private GridViewAdapter gridAdapter;
    private final int initial_count = 1;
    private final String recyclerViewStateStateKey = "recyclerViewState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        networkBroadcast = new NetworkStateChangeReceiver(parentView);

        Utilities.setupToolbarAndNavigationBar(this, toolbar, navigationView, drawerLayout);

        setupRecyclerView();

        Utilities.doNetworkRequest(initial_count, parentView, mainContentLayout, errorLayout,MainActivity.this);

        loadDataMoreOnScroll();
    }

    private void setupRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToPadding(false);

        gridAdapter = new GridViewAdapter(this, AllData.getInstance().getPhotoData(), MainActivity.this, recyclerView);

        recyclerView.setAdapter(gridAdapter);
    }

    private void loadDataMoreOnScroll() {
        gridAdapter.setOnLoadMoreListener(new GridViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(final int totalCount) {
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.doNetworkRequest(totalCount, parentView, mainContentLayout, errorLayout,MainActivity.this);
                        gridAdapter.setLoaded();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void itemClick(final int currentPosition, final ImageView imageView) {
        Intent intent = new Intent(this, SnapDetailsActivity.class);
        intent.putExtra("currentPosition", currentPosition);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Utilities.registerNetworkStateChangerReciever(this, networkBroadcast);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bundle != null) {
            Parcelable listState = bundle.getParcelable(recyclerViewStateStateKey);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        if (SnapDetailsActivity.position != -1) {
            recyclerView.smoothScrollToPosition(SnapDetailsActivity.position);
            SnapDetailsActivity.position = -1;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bundle = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(recyclerViewStateStateKey, listState);
        drawerLayout.closeDrawers();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcast);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhotoPaginationEventBus event) {
        gridAdapter.notifyItemRangeInserted(event.getInitialCount(), 10);
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

    @SuppressWarnings("unused")
    @OnClick(R.id.retry)
    public void retry(View view) {
        Utilities.doNetworkRequest(initial_count, parentView, mainContentLayout, errorLayout,MainActivity.this);
    }
}