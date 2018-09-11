package com.example.harpalsingh.codingchallengeharpalsingh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.harpalsingh.codingchallengeharpalsingh.Activities.SnapDetailsActivity;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.ArrayList;

public class SnapDetailAdapter extends RecyclerView.Adapter<SnapDetailAdapter.SnapDetailViewHolder> {

    private final ArrayList<PhotoDatum> data;
    private final RequestManager glide;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private int totalItemCount;
    private int lastPositions;
    private RecyclerView recyclerView;

    private Activity activity;

    public SnapDetailAdapter(Context context, ArrayList<PhotoDatum> photoData, RecyclerView recyclerView) {
        this.data = photoData;
        glide = Glide.with(context);
        setupScrolling(recyclerView);
        this.recyclerView = recyclerView;
        activity = (Activity) context;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    private void setupScrolling(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();

                    lastPositions = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (!loading & totalItemCount == lastPositions + 1) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore(totalItemCount);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int totalCount);
    }

    @NonNull
    @Override
    public SnapDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_pager_item_layout, parent, false);

        int orientation = activity.getResources().getConfiguration().orientation;
        int width = recyclerView.getWidth();
        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            params.width = (int) (width * 0.9);
            view.setPadding(15, 15, 15, 15);
            view.setLayoutParams(params);
        }


        return new SnapDetailViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SnapDetailViewHolder holder, int position) {
        holder.setData(holder.getAdapterPosition());
        SnapDetailsActivity.position = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SnapDetailViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView userNameTextView;
        final ImageView userImage;
        final TextView likesTextView;
        final TextView portfolioTextView;
        final TextView bioTextView;

        SnapDetailViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.viewPagerImage);
            this.userNameTextView = itemView.findViewById(R.id.user_name);
            this.userImage = itemView.findViewById(R.id.user_image);
            this.likesTextView = itemView.findViewById(R.id.likes);
            this.portfolioTextView = itemView.findViewById(R.id.portfolio);
            this.bioTextView = itemView.findViewById(R.id.bio_text);
        }

        void setData(int position) {

            glide.load(data.get(position).getUrls().getRegular())
                    .thumbnail(glide.load(R.drawable.image_placeholder)).into(imageView);

            glide.load(data.get(position).getUser().getProfileImage().getMedium()).apply(RequestOptions
                    .circleCropTransform()).into(userImage);

            String firstName = "By - " + checkNull(data.get(position).getUser().getFirstName(), "Unknown");
            String likes = String.valueOf(data.get(position).getLikes());
            String userBiography = "Biography :" + checkNull(data.get(position).getUser().getBio(), "No information Available");
            String url = data.get(position).getUser().getPortfolioUrl();

            userNameTextView.setText(firstName);
            likesTextView.setText(likes);
            bioTextView.setText(userBiography);

            if (url != null) {
                String linkedText = String.format("Explore more: <a href=\"%s\">" + url + "</a> ", url);
                portfolioTextView.setText(Html.fromHtml(linkedText));
                portfolioTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        private String checkNull(String data, String message) {
            data = data != null ? data : message;
            return data;
        }
    }
}