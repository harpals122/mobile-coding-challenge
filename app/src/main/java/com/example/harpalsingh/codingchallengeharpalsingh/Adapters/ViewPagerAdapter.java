package com.example.harpalsingh.codingchallengeharpalsingh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    private final Context context;
    private final ArrayList<PhotoDatum> photoData;
    Activity activity;

    public ViewPagerAdapter(Context context, ArrayList<PhotoDatum> photoData) {
        this.context = context;
        this.photoData = photoData;
        this.activity= (Activity) context;


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
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = Objects.requireNonNull(layoutInflater).inflate(R.layout.view_pager_item_layout, container, false);
        final ImageView imageView = row.findViewById(R.id.viewPagerImage);
        final TextView userNameTextView = row.findViewById(R.id.user_name);
        final ImageView userImage = row.findViewById(R.id.user_image);
        final TextView bioTextView = row.findViewById(R.id.bio_text);
        final TextView likesTextView = row.findViewById(R.id.likes);
        final TextView portfolioTextView = row.findViewById(R.id.portfolio);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getSharedElementEnterTransition().setDuration(550);
            imageView.setTransitionName("photo_image");
        }
        Glide.with(context)
                .load(photoData.get(position).getUrls().getRegular())
                .into(imageView);
        userNameTextView.setText("By - "+ photoData.get(position).getUser().getFirstName());
        likesTextView.setText(""+photoData.get(position).getLikes());
        Glide.with(context)
                .load(photoData.get(position).getUser().getProfileImage().getMedium()).apply(RequestOptions.circleCropTransform())
                .into(userImage);

        bioTextView.setText("Bio - "+ photoData.get(position).getUser().getBio());



        String dynamicUrl = photoData.get(position).getUser().getPortfolioUrl();
        if(dynamicUrl!=null) {
            String linkedText = String.format("Explore more: <a href=\"%s\">" + dynamicUrl + "</a> ", dynamicUrl);
            portfolioTextView.setText(Html.fromHtml(linkedText));
            portfolioTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }



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