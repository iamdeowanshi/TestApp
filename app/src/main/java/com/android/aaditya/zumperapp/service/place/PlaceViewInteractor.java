package com.android.aaditya.zumperapp.service.place;

import com.android.aaditya.zumperapp.base.ViewInteractor;
import com.android.aaditya.zumperapp.model.Place;

import java.util.List;

/**
 * Created by aaditya on 11/10/17.
 */

public interface PlaceViewInteractor extends ViewInteractor{

    void showProgress();
    void hideProgress();
    void onDetails(Place place);
    void onResult(List<Place> places);
}
