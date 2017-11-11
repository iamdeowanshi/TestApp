package com.android.aaditya.zumperapp.service.place;

import com.android.aaditya.zumperapp.base.Presenter;
import com.android.aaditya.zumperapp.model.Place;

/**
 * Created by aaditya on 11/10/17.
 */

public interface PlacePresenter extends Presenter<PlaceViewInteractor> {

    void getPlaces(String location, String type, String radius);
    void getDetails(Place place);

}
