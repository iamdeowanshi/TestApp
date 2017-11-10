package com.android.aaditya.zumperapp;

import android.app.Activity;
import android.os.Bundle;

import com.android.aaditya.zumperapp.base.BaseActivity;
import com.android.aaditya.zumperapp.model.Place;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends BaseActivity implements PlaceViewInteractor {

    private PlacePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new PlacePresenterImpl();
        presenter.attachViewInteractor(this);

        presenter.getPlaces("37.77,-122.42","restaurant","50000");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onDetails(Place place) {

    }

    @Override
    public void onResult(List<Place> places) {
        Timber.d(String.valueOf(places.size()));
    }
}
