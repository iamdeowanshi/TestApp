package com.android.aaditya.zumperapp;

import com.android.aaditya.zumperapp.base.BasePresenter;
import com.android.aaditya.zumperapp.model.Place;
import com.android.aaditya.zumperapp.service.ApiModule;
import com.android.aaditya.zumperapp.service.GoogleService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by aaditya on 11/10/17.
 */

public class PlacePresenterImpl extends BasePresenter<PlaceViewInteractor> implements PlacePresenter {

    private GoogleService service;
    private JsonObject responseNearBy;

    public PlacePresenterImpl() {
        service = ApiModule.getInstance().getGoogleService();
    }

    @Override
    public void getPlaces(String location, String type, String radius) {
        Observable<ResponseBody> observable = service.getNearBy(location, radius, type,Config.GOOGLE_API_KEY);
        getViewInteractor().showProgress();

        new CompositeDisposable().add(observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<ResponseBody>() {
                                @Override
                                public void onNext(@NonNull ResponseBody responseBody) {
                                    try {
                                        responseNearBy = (JsonObject) new JsonParser().parse(String.valueOf((responseBody).string()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    List<Place> places = new ArrayList<>();
                                    JsonArray results = (JsonArray) responseNearBy.get("results");

                                    for (JsonElement element : results) {
                                        Place place = new Place();
                                        JsonObject item = element.getAsJsonObject();
                                        JsonObject geometry = item.getAsJsonObject("geometry");
                                        place.setLat(geometry.getAsJsonObject("location").get("lat").getAsString());
                                        place.setLng(geometry.getAsJsonObject("location").get("lng").getAsString());

                                        place.setName(item.get("name").getAsString());
                                        place.setPlaceid(item.get("place_id").getAsString());
                                        place.setIcon(item.get("icon").getAsString());

                                        places.add(place);
                                    }

                                    getViewInteractor().hideProgress();
                                    getViewInteractor().onResult(places);

                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    getViewInteractor().hideProgress();
                                    e.printStackTrace();
                                }

                                @Override
                                public void onComplete() {

                                }
                            }));
    }

    @Override
    public void getDetails(String placeId) {

        Observable<ResponseBody> observable = service.getDetails(placeId,Config.GOOGLE_API_KEY);

        new CompositeDisposable().add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
