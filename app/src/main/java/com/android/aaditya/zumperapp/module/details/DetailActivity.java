package com.android.aaditya.zumperapp.module.details;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.aaditya.zumperapp.R;
import com.android.aaditya.zumperapp.base.BaseActivity;
import com.android.aaditya.zumperapp.model.Place;
import com.android.aaditya.zumperapp.service.place.PlacePresenter;
import com.android.aaditya.zumperapp.service.place.PlacePresenterImpl;
import com.android.aaditya.zumperapp.service.place.PlaceViewInteractor;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;
import timber.log.Timber;

public class DetailActivity extends BaseActivity implements PlaceViewInteractor {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.progress)
    RelativeLayout progress;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.recycler_reviews)
    RecyclerView recyclerViewReview;

    private PlacePresenter presenter;
    private ImageAdapter imageAdapter;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        presenter = new PlacePresenterImpl();
        presenter.attachViewInteractor(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));
        Bundle extras = getIntent().getExtras();
        Place place = new Gson().fromJson(extras.getString("place"), Place.class);

        presenter.getDetails(place);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onDetails(Place place) {
        Timber.d(place.getName());
        collapsingToolbarLayout.setTitle(place.getName());
        loadData(place);
    }

    @Override
    public void onResult(List<Place> places) {

    }

    /**
     * Load place details into view.
     * @param place
     */
    private void loadData(Place place) {
        imageAdapter = new ImageAdapter(this, place.getPhotos());
        viewPager.setAdapter(imageAdapter);
        indicator.setViewPager(viewPager);

        reviewAdapter = new ReviewAdapter(this, place.getReviews());
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewReview.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReview.setAdapter(reviewAdapter);
        recyclerViewReview.setNestedScrollingEnabled(false);


        textName.setText(place.getName());
        rating.setText(String.valueOf(place.getRating()));
        ratingBar.setRating(place.getRating());
        address.setText(place.getAddress());
        phone.setText(place.getNumber());

    }
}
