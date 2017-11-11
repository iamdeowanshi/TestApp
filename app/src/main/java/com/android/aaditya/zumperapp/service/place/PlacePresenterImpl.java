package com.android.aaditya.zumperapp.service.place;

import com.android.aaditya.zumperapp.Config;
import com.android.aaditya.zumperapp.base.BasePresenter;
import com.android.aaditya.zumperapp.model.Place;
import com.android.aaditya.zumperapp.model.Review;
import com.android.aaditya.zumperapp.service.ApiModule;
import com.android.aaditya.zumperapp.service.GoogleService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
    private JsonObject responseDetail;

    public PlacePresenterImpl() {
        service = ApiModule.getInstance().getGoogleService();
    }

    /**
     * Fetching list of nearby by search.
     * @param location
     * @param type
     * @param radius
     */
    @Override
    public void getPlaces(String location, String type, String radius) {
        Observable<ResponseBody> observable = service.getNearBy(location, radius, type,"distance", Config.GOOGLE_API_KEY);
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

    /**
     * Fetching details for a place using placeId.
     * @param place
     */
    @Override
    public void getDetails(final Place place) {

        Observable<ResponseBody> observable = service.getDetails(place.getPlaceid(),Config.GOOGLE_API_KEY);

        new CompositeDisposable().add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            responseDetail = (JsonObject) new JsonParser().parse(String.valueOf((responseBody).string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        JsonObject result = responseDetail.getAsJsonObject("result");
                        place.setAddress(result.get("formatted_address").getAsString());
                        place.setNumber(result.get("international_phone_number").getAsString());
                        place.setRating(result.get("rating").getAsFloat());

                        Type listType = new TypeToken<List<Review>>() {
                        }.getType();
                        List<Review> reviews = new Gson().fromJson(result.get("reviews").toString(), listType);
                        place.setReviews(reviews);

                        JsonArray photos = (JsonArray) result.get("photos");
                        List<String> images = new ArrayList<>();
                        for (JsonElement element: photos)
                            images.add(element.getAsJsonObject().get("photo_reference").getAsString());

                        place.setPhotos(images);
                        getViewInteractor().hideProgress();
                        getViewInteractor().onDetails(place);

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
}
