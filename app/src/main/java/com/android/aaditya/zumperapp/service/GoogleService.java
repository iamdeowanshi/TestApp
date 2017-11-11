package com.android.aaditya.zumperapp.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GoogleService {

    @GET("nearbysearch/json")
    Observable<ResponseBody> getNearBy(@Query("location") String location, @Query("radius") String radius,@Query("type") String type,@Query("rankBy") String distance, @Query("key") String key);

    @GET("details/json")
    Observable<ResponseBody> getDetails(@Query("placeid") String placeid, @Query("key") String key);

}
