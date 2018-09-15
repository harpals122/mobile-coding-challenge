package com.example.harpalsingh.codingchallengeharpalsingh.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.harpalsingh.codingchallengeharpalsingh.activities.MainActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.models.PhotoDatum;
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
        this.itemClick = mainActivity;
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
        final ImageView placeHolder;

        GridViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            placeHolder = itemView.findViewById(R.id.image_progress_Bar);
            constraintLayout = itemView.findViewById(R.id.parent_constraint);
        }

        private void bindData(final int position) {

            glide.load(data.get(position).getUrls().getThumb()).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            placeHolder.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            placeHolder.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(image);

            int height = data.get(position).getHeight();
            int width = data.get(position).getWidth();
            final String ratio = String.format(Locale.getDefault(), "%d:%d", width, height);
            constraintSet.clone(constraintLayout);
            constraintSet.setDimensionRatio(image.getId(), ratio);
            constraintSet.applyTo(constraintLayout);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.itemClick(position, image);
                }
            });
        }
    }
}