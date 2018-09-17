package com.example.harpalsingh.codingchallengeharpalsingh.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.harpalsingh.codingchallengeharpalsingh.R;
import com.example.harpalsingh.codingchallengeharpalsingh.adapters.SnapDetailAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.eventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.interfaces.OnLoadMoreListener;
import com.example.harpalsingh.codingchallengeharpalsingh.models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoData;
import com.example.harpalsingh.codingchallengeharpalsingh.utilities.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SnapDetailsActivity extends AppCompatActivity {

    public static int POSITION = -1;
    private final Handler handler = new Handler();
    @BindView(R.id.grid_details_recycler_viewpager)
    RecyclerView recyclerView;
    @BindView(R.id.load_more)
    ProgressBar progressBar;
    @BindView(R.id.snap_detail_parent_view)
    View snapDetailParentView;
    private SnapDetailAdapter snapDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_detail);
        ButterKnife.bind(this);

        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("currentPosition", 1);

        ArrayList<PhotoData> photoData = AllData.getInstance().getPhotoData();

        final LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayout);

        snapDetailAdapter = new SnapDetailAdapter(this, photoData, recyclerView);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(snapDetailAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(position);

        snapDetailAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final int totalCount) {
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.doNetworkRequest(totalCount, snapDetailParentView, null, null, null);
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("POSITION", POSITION);
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
    }
}