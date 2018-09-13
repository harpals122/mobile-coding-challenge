package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.SnapDetailAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;
import com.example.harpalsingh.codingchallengeharpalsingh.Utilities.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unused")
public class SnapDetailsActivity extends AppCompatActivity {

    @BindView(R.id.gridDetailsRecyclerViewpager)
    RecyclerView recyclerView;
    @BindView(R.id.loadMore)
    ProgressBar progressBar;
    @BindView(R.id.snapDetailParentView)
    View snapDetailParentView;

    private SnapDetailAdapter snapDetailAdapter;
    public static int position = -1;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_detail);
        ButterKnife.bind(this);

        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("currentPosition", 1);

        ArrayList<PhotoDatum> photoData = AllData.getInstance().getPhotoData();

        final LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayout);

        snapDetailAdapter = new SnapDetailAdapter(this, photoData, recyclerView);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(snapDetailAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(position);

        snapDetailAdapter.setOnLoadMoreListener(new SnapDetailAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(final int totalCount) {
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.doNetworkRequest(totalCount, snapDetailParentView,null,null,null);
                        snapDetailAdapter.setLoaded();
                    }
                }, 2000);
            }
        });
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
        snapDetailAdapter.notifyItemRangeInserted(event.getInitialCount(), 10);
        progressBar.setVisibility(View.GONE);
    }
}