package com.example.harpalsingh.codingchallengeharpalsingh.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.harpalsingh.codingchallengeharpalsingh.Activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.Activities.SnapDetailsActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.GridViewAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.EventBus.PhotoPaginationEventBus;
import com.example.harpalsingh.codingchallengeharpalsingh.GenericApiCalls.GenericApiCalls;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SnapGridFragment extends Fragment implements GridViewAdapter.ItemClick{

    private Bundle bundle = new Bundle();
    private final String recyclerViewStateStateKey = "recyclerViewState";
    private final Random random = new Random();
    private Handler handler;


    @BindView(R.id.gridRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loadin_progress_bar)
    ProgressBar progressBar;
    private GridViewAdapter gridAdapter;

    public SnapGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snap_grid, container, false);
        ButterKnife.bind(this, view);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        gridAdapter = new GridViewAdapter(getContext(), AllData.getInstance().getPhotoData(), SnapGridFragment.this, recyclerView);
        recyclerView.setAdapter(gridAdapter);
        recyclerView.setHasFixedSize(true);

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
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void loadData(final int totalCount) {
        int randomNumber = random.nextInt(99 - 1) + 65;
        GenericApiCalls genericEventBus = new GenericApiCalls(getContext());
        genericEventBus.doUnsplashRequest(randomNumber, totalCount);
    }

    @Override
    public void itemClick(final int currentPosition, final ImageView imageView) {
      /*  Fragment newFragment = new SnapDetailFragment();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.main_content_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();*/

      Intent intent = new Intent(getActivity(), SnapDetailsActivity.class);
      getActivity().startActivity(intent);

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



}
