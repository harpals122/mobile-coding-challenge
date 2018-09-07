package com.example.harpalsingh.codingchallengeharpalsingh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.harpalsingh.codingchallengeharpalsingh.Activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class GridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<PhotoDatum> data;
    private final ItemClick itemClick;
    private final ConstraintSet constraintSet = new ConstraintSet();
    private Bitmap bitmap = null;

    public GridViewAdapter(Context context, ArrayList<PhotoDatum> photoDatum, MainActivity activity) {
        this.context = context;
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final GridViewHolder viewHolder = (GridViewHolder) holder;

        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(data.get(viewHolder.getAdapterPosition()).getUrls().getThumb());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final String ratio = String.format(Locale.getDefault(), "%d:%d", bitmap.getWidth(), bitmap.getHeight());
                constraintSet.clone(viewHolder.constraintLayout);
                constraintSet.setDimensionRatio(viewHolder.image.getId(), ratio);
                constraintSet.applyTo(viewHolder.constraintLayout);

                Glide.with(context).clear(viewHolder.image);

                Glide.with(context).load(data.get(viewHolder.getAdapterPosition()).getUrls().getThumb()).into(viewHolder.image);

                viewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        itemClick.itemClick(viewHolder.getAdapterPosition());
                    }
                });
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        Toast.makeText(context, "dfdsaf", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClick {
        void itemClick(int position);
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
}










