package com.example.harpalsingh.codingchallengeharpalsingh.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    private final Context context;
    private final ArrayList<PhotoDatum> photoData;

    public ViewPagerAdapter(Context context, ArrayList<PhotoDatum> photoData) {
        this.context = context;
        this.photoData = photoData;
    }

    @Override
    public float getPageWidth(int position) {
        return 1.0f;
    }

    @Override
    public int getCount() {
        return photoData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = Objects.requireNonNull(layoutInflater).inflate(R.layout.view_pager_item_layout, container, false);
        ImageView imageView = row.findViewById(R.id.viewPagerImage);
        Glide.
                with(context)
                .load(photoData.get(position).getUrls().getRegular())
                .into(imageView);

        container.addView(row);
        return row;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}