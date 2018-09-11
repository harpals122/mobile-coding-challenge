package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.SnapDetailAdapter;
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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnapDetailsActivity extends AppCompatActivity {

    public static int position = -1;

    @BindView(R.id.gridDetailsRecyclerViewpager)
    RecyclerView recyclerView;

    @BindView(R.id.loadMore)
    ProgressBar progressBar;

    private final Handler handler = new Handler();
    private final Random random = new Random();
    private SnapDetailAdapter snapDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_detail);

        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("currentPosition", 1);

        ButterKnife.bind(this);

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
                        loadData(totalCount);
                        snapDetailAdapter.setLoaded();
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
        snapDetailAdapter.notifyItemRangeInserted(event.getTotal_count(), 10);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent();
        intent.putExtra("updatedPosition",position);
        setResult(100,intent);
        super.onBackPressed();
    }
}







