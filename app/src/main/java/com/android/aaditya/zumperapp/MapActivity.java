package com.android.aaditya.zumperapp;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.aaditya.zumperapp.base.BaseActivity;
import com.android.aaditya.zumperapp.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class MapActivity extends BaseActivity implements PlaceViewInteractor, OnMapReadyCallback {

    private PlacePresenter presenter;
    private GoogleMap googleMap;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new PlacePresenterImpl();
        presenter.attachViewInteractor(this);

        presenter.getPlaces("37.77,-122.42","restaurant","5000");

        initializeMap();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDetails(Place place) {

    }

    @Override
    public void onResult(List<Place> places) {
        Timber.d(String.valueOf(places.size()));
        addMarker(places);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        LatLng currentLocation = new LatLng(37.77,-122.42);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).tilt(90).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addMarker(List<Place> places) {
        for (final Place place : places) {
            double latitude = Double.parseDouble(place.getLat());
            double longitude = Double.parseDouble(place.getLng());

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(place.getName().split(" ")[0])
                    .icon(BitmapDescriptorFactory.defaultMarker()));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Bundle bundle = new Bundle();
                    bundle.putString("placeId", place.getPlaceid());
                    startActivity(DetailActivity.class, bundle );
                    return true;
                }
            });
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
