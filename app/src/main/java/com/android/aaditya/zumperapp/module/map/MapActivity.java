package com.android.aaditya.zumperapp.module.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.aaditya.zumperapp.R;
import com.android.aaditya.zumperapp.base.BaseActivity;
import com.android.aaditya.zumperapp.model.Place;
import com.android.aaditya.zumperapp.module.details.DetailActivity;
import com.android.aaditya.zumperapp.service.place.PlacePresenter;
import com.android.aaditya.zumperapp.service.place.PlacePresenterImpl;
import com.android.aaditya.zumperapp.service.place.PlaceViewInteractor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class MapActivity extends BaseActivity implements PlaceViewInteractor,GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener, OnMapReadyCallback {

    private PlacePresenter presenter;
    private GoogleMap googleMap;
    private double cameraLat;
    private double cameraLng;
    private boolean isMoving;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        presenter = new PlacePresenterImpl();
        presenter.attachViewInteractor(this);

        cameraLat = 37.77;
        cameraLng =-122.42;
        presenter.getPlaces("37.77,-122.42","restaurant","500");

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

        //Removing other marker from map.
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);


        LatLng currentLocation = new LatLng(37.77,-122.42);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(15).tilt(90).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraIdleListener(this);
    }

    /**
     * Add marker for each place on map.
     * @param places
     */
    private void addMarker(List<Place> places) {
        googleMap.clear();
        for (final Place place : places) {
            double latitude = Double.parseDouble(place.getLat());
            double longitude = Double.parseDouble(place.getLng());

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(place.getName())
                    .snippet(place.getAddress())
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapIcon(place))));
            marker.setTag(place);

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Bundle bundle = new Bundle();
                    bundle.putString("place", new Gson().toJson(marker.getTag()));
                    startActivity(DetailActivity.class, bundle );
                    return true;
                }
            });

        }
    }

    /**
     * Initializing map.
     */
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Returns bitmap icon for place.
     * @param place
     * @return
     */
    private Bitmap getBitmapIcon(Place place){
        FrameLayout view = (FrameLayout)findViewById(R.id.framelayout);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(place.getName());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @Override
    public void onCameraIdle() {
        if (!isMoving)
            return;

        isMoving = false;

        Location start = new Location("start");
        start.setLatitude(cameraLat);
        start.setLongitude(cameraLng);

        Location end = new Location("end");
        end.setLatitude(googleMap.getCameraPosition().target.latitude);
        end.setLongitude(googleMap.getCameraPosition().target.longitude);

        float distance = start.distanceTo(end);

        // If distance less than 700 metres don't call api.
        if (distance < 700)
            return;

        cameraLat = end.getLatitude();
        cameraLng = end.getLongitude();

        presenter.getPlaces(String.valueOf(cameraLat + "," + cameraLng),"restaurant","500");

    }

    @Override
    public void onCameraMoveStarted(int i) {
        Timber.d("started");
        // Flag for camera moved.
        isMoving = true;
    }
}
