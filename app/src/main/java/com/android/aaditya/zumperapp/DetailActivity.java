package com.android.aaditya.zumperapp;

import android.os.Bundle;

import com.android.aaditya.zumperapp.base.BaseActivity;
import com.android.aaditya.zumperapp.model.Place;

import java.util.List;

import timber.log.Timber;

public class DetailActivity extends BaseActivity implements PlaceViewInteractor {

    private PlacePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        presenter = new PlacePresenterImpl();
        presenter.attachViewInteractor(this);

        Bundle extras = getIntent().getExtras();
        String placeId = extras.getString("placeId");

        presenter.getDetails(placeId);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onDetails(Place place) {
        Timber.d(place.getName());

    }

    @Override
    public void onResult(List<Place> places) {

    }
}
