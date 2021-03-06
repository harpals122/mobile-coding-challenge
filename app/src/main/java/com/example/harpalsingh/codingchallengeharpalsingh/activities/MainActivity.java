package com.example.harpalsingh.codingchallengeharpalsingh.activities;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.harpalsingh.codingchallengeharpalsingh.R;
import com.example.harpalsingh.codingchallengeharpalsingh.adapters.GridViewAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.eventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.interfaces.OnLoadMoreListener;
import com.example.harpalsingh.codingchallengeharpalsingh.models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.utilities.NetworkStateChangeReceiver;
import com.example.harpalsingh.codingchallengeharpalsingh.utilities.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements GridViewAdapter.ItemClick {

    private final Handler handler = new Handler();
    private final int initial_count = 1;
    private final String recyclerViewStateStateKey = "recyclerViewState";
    @BindView(R.id.app_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.grid_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.main_layout)
    View parentView;
    @BindView(R.id.main_content_container)
    RelativeLayout mainContentLayout;
    @BindView(R.id.retry_parent_view)
    LinearLayout errorLayout;
    @BindView(R.id.retry)
    Button retry;
    int request_code = 100;
    private Bundle bundle = new Bundle();
    private BroadcastReceiver networkBroadcast;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        networkBroadcast = new NetworkStateChangeReceiver(parentView);

        Utilities.setupToolbarAndNavigationBar(this, toolbar, navigationView, drawerLayout);

        setupRecyclerView();

        Utilities.doNetworkRequest(initial_count, parentView, mainContentLayout, errorLayout, MainActivity.this);

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
        gridAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final int totalCount) {
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.doNetworkRequest(totalCount, parentView, mainContentLayout, errorLayout, MainActivity.this);
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
        startActivityForResult(intent, request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getIntExtra("POSITION", 0) != -1)
                    recyclerView.smoothScrollToPosition(SnapDetailsActivity.POSITION);
            }
        }
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

    @OnClick(R.id.retry)
    public void retry(View view) {
        Utilities.doNetworkRequest(initial_count, parentView, mainContentLayout, errorLayout, MainActivity.this);
    }
}