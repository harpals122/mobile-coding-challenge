package com.example.harpalsingh.codingchallengeharpalsingh.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.harpalsingh.codingchallengeharpalsingh.APILayer.RetrofitServices;
import com.example.harpalsingh.codingchallengeharpalsingh.Adapters.GridViewAdapter;
import com.example.harpalsingh.codingchallengeharpalsingh.Interfaces.KeyConfig;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.AllData;
import com.example.harpalsingh.codingchallengeharpalsingh.Models.PhotoDatum;
import com.example.harpalsingh.codingchallengeharpalsingh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GridViewAdapter.ItemClick {
    private static Bundle bundle;
    private final String recyclerViewStateStateKey = "recyclerViewState";
    @BindView(R.id.app_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.gridRecyclerView)
    RecyclerView recyclerView;

    private GridViewAdapter gridAdapter;
    private final ArrayList<PhotoDatum> photoData = new ArrayList<>();

    private final int visibleThreshold = 2;
    private int lastVisibleItem;
    private int totalItemCount;
    private int[] lastPositions = null;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<PhotoDatum> photoDatum;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        loadData();

        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        totalItemCount = staggeredGridLayoutManager.getItemCount();

                        if (lastPositions == null) {
                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        }

                        lastPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                        lastVisibleItem = Math.max(lastPositions[0], lastPositions[1]);

                        if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            loadMoreRandomData();

                        }
                    }
                }
            });
        }
    }

    private void loadMoreRandomData() {

        int randomNumber = random.nextInt(99 - 1) + 65;

        Call<List<PhotoDatum>> callPlaces = RetrofitServices.getNYServiceInstance().getPhotos(KeyConfig.client_key, randomNumber);
        callPlaces.enqueue(new Callback<List<PhotoDatum>>() {
            @Override
            public void onResponse(Call<List<PhotoDatum>> call, final Response<List<PhotoDatum>> response) {
                if (response.code() == 200) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AllData.getInstance().setPhotoData((ArrayList<PhotoDatum>) response.body());

                            gridAdapter.notifyItemRangeInserted(totalItemCount, 2);
                        }
                    },1000);

                } else {
                    Toast.makeText(MainActivity.this, "Make sure you have proper Unauthorization key to access unsplash API ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PhotoDatum>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong. Error message :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadData() {
        int randomNumber = random.nextInt(99 - 1) + 65;
        Call<List<PhotoDatum>> callPlaces = RetrofitServices.getNYServiceInstance().getPhotos(KeyConfig.client_key, randomNumber);
        callPlaces.enqueue(new Callback<List<PhotoDatum>>() {
            @Override
            public void onResponse(Call<List<PhotoDatum>> call, Response<List<PhotoDatum>> response) {
                if (response.code() == 200) {
                    photoDatum = (ArrayList<PhotoDatum>) response.body();
                    AllData.getInstance().setPhotoData(photoDatum);
                    gridAdapter = new GridViewAdapter(getApplicationContext(), AllData.getInstance().getPhotoData(), MainActivity.this);
                    recyclerView.setAdapter(gridAdapter);
                    gridAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Make sure you have proper Unauthorization key to access unsplash API ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PhotoDatum>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong. Error message :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.facebook:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(final int currentPosition, final ImageView imageView) {
        Intent intent = new Intent(MainActivity.this, SnapDetailsActivity.class);
        Bundle bundle = new Bundle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bundle.putInt("id", currentPosition);
            intent.putExtras(bundle);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, "photo_image");
            startActivity(intent, options.toBundle());
        } else {
            bundle.putInt("id", currentPosition);
            bundle.putSerializable("photoData", photoData);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
    protected void onPause() {
        super.onPause();
        bundle = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(recyclerViewStateStateKey, listState);
    }
}







