package com.example.harpalsingh.codingchallengeharpalsingh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.harpalsingh.codingchallengeharpalsingh.Activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Callback;

public class GridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<PhotoDatum> data;
    private final ItemClick itemClick;
    private final ConstraintSet constraintSet = new ConstraintSet();
    private Bitmap bitmap = null;


    private final int GRID_VIEW = 1;
    private final int PROGRESS_VIEW = 0;

    public GridViewAdapter(Context context , ArrayList<PhotoDatum> photoDatum, MainActivity activity) {
        this.context =context;
        this.data = photoDatum;
        this.itemClick = activity;

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final GridViewHolder viewHolder = (GridViewHolder) holder;


                try {
                    URL url = new URL(data.get(position).getUrls().getThumb());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final String ratio = String.format(Locale.getDefault(), "%d:%d", bitmap.getWidth(), bitmap.getHeight());
                constraintSet.clone(viewHolder.constraintLayout);
                constraintSet.setDimensionRatio(viewHolder.image.getId(), ratio);
                constraintSet.applyTo(viewHolder.constraintLayout);

                Glide.with(context).clear(viewHolder.image);

                Glide.with(context).load(data.get(position).getUrls().getThumb()).
                        into(viewHolder.image);


        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.itemClick(position,viewHolder.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClick {
        void itemClick(final int position, final ImageView imageView);
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final ConstraintLayout constraintLayout;

        GridViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            constraintLayout = itemView.findViewById(R.id.parentContsraint);
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}










