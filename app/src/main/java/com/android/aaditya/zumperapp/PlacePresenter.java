package com.android.aaditya.zumperapp;

import com.android.aaditya.zumperapp.base.BasePresenter;
import com.android.aaditya.zumperapp.base.Presenter;

/**
 * Created by aaditya on 11/10/17.
 */

public interface PlacePresenter extends Presenter<PlaceViewInteractor> {

    void getPlaces(String location, String type, String radius);
    void getDetails(String placeId);

}
