package com.example.harpalsingh.codingchallengeharpalsingh.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.harpalsingh.codingchallengeharpalsingh.Activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.GridViewAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.SnapDetailAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.GenericApiCalls.GenericApiCalls;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SnapDetailFragment extends Fragment {
    ArrayList<PhotoDatum> photoData;
    @BindView(R.id.gridDetailsRecyclerViewpager)
    RecyclerView recyclerView;

    public SnapDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_snap_detail, container, false);
        ButterKnife.bind(this,view);

        photoData = (ArrayList<PhotoDatum>) AllData.getInstance().getPhotoData();

        final LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(linearLayout);
        SnapDetailAdapter  snapDetailAdapter= new SnapDetailAdapter(getContext(), AllData.getInstance().getPhotoData());

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        recyclerView.setAdapter(snapDetailAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.smoothScrollToPosition(7);

        return view;
    }
}
