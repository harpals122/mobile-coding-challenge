package com.example.harpalsingh.codingchallengeharpalsingh.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.harpalsingh.codingchallengeharpalsingh.Activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.ArrayList;
import java.util.Locale;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {

    private final ArrayList<PhotoDatum> data;
    private final ItemClick itemClick;
    private final ConstraintSet constraintSet = new ConstraintSet();
    private final int visibleThreshold = 6;
    private final RequestManager glide;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private int lastVisibleItem;
    private int totalItemCount;
    private int[] lastPositions = null;

    public GridViewAdapter(Context context, ArrayList<PhotoDatum> photoDatum, MainActivity mainActivity, RecyclerView recyclerView) {
        this.data = photoDatum;
        this.itemClick = (ItemClick) mainActivity;
        glide = Glide.with(context);

        setupScrolling(recyclerView);
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.bindData(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    private void setupScrolling(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                }
            });
        }
    }

    public interface ItemClick {
        void itemClick(final int position, final ImageView imageView);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int totalCount);
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final ConstraintLayout constraintLayout;
        final ProgressBar progressBar;

        GridViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progressBar);
            constraintLayout = itemView.findViewById(R.id.parentConstraint);
        }

        private void bindData(final int position) {
            glide.asBitmap().load(data.get(position).getUrls().getThumb()).
                    listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();
                            final String ratio = String.format(Locale.getDefault(), "%d:%d", w, h);
                            constraintSet.clone(constraintLayout);
                            constraintSet.setDimensionRatio(image.getId(), ratio);
                            constraintSet.applyTo(constraintLayout);
                            image.setImageBitmap(bitmap);
                            return false;
                        }
                    }).into(image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.itemClick(position, image);
                }
            });
        }
    }
}










