package com.example.harpalsingh.codingchallengeharpalsingh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private final int GRID_VIEW = 1;
    private final int PROGRESS_VIEW = 0;

    private final int visibleThreshold = 6;
    private int lastVisibleItem;
    private int totalItemCount;
    private int[] lastPositions = null;

    public GridViewAdapter(Context context , ArrayList<PhotoDatum> photoDatum, MainActivity activity,RecyclerView recyclerView) {
        this.context =context;
        this.data = photoDatum;
        this.itemClick = activity;

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {

            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = staggeredGridLayoutManager.getItemCount();

                            if (lastPositions == null) {
                                lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                            }

                            lastPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                            lastVisibleItem = Math.max(lastPositions[0], lastPositions[1]);

                            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore(totalItemCount);
                                }
                                loading = true;
                            }
                            else
                                {

                                }
                        }
                    });
        }

    }



    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? GRID_VIEW : PROGRESS_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == GRID_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.grid_item_layout, parent, false);

            viewHolder = new GridViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progrss_bar_layout, parent, false);

            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof GridViewHolder) {
            final GridViewHolder viewHolder = (GridViewHolder) holder;

            Glide.with(context).clear(viewHolder.image);
            Glide.with(context).
                    asBitmap().
                    load(data.get(position).getUrls().getThumb()).
                    listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            viewHolder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolder.progressBar.setVisibility(View.GONE);
                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();
                            final String ratio = String.format(Locale.getDefault(), "%d:%d", w, h);
                            constraintSet.clone(viewHolder.constraintLayout);
                            constraintSet.setDimensionRatio(viewHolder.image.getId(), ratio);
                            constraintSet.applyTo(viewHolder.constraintLayout);
                            viewHolder.image.setImageBitmap(bitmap);
                            return false;
                        }
                    }).
                    into(viewHolder.image);

            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.itemClick(position,viewHolder.image);
                }
            });

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }



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
        final ProgressBar progressBar;

        GridViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progressBar);
            constraintLayout = itemView.findViewById(R.id.parentContsraint);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public interface OnLoadMoreListener {
        void onLoadMore(int totalCount);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

}










