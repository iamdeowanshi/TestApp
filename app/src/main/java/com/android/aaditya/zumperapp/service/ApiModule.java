package com.android.aaditya.zumperapp.service;

import com.android.aaditya.zumperapp.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by aaditya on 10/20/17.
 */

public class ApiModule {

    private static ApiModule apiModule;

    private ApiModule(){
        //Prevent form the reflection api.
        if (apiModule != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static ApiModule getInstance(){
        if (apiModule == null){ //if there is no instance available... create new one
            apiModule = new ApiModule();
        }

        return apiModule;
    }

    public GoogleService getGoogleService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .client(provideOkHttpClient(provideInterceptors()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(GoogleService.class);
    }

    public OkHttpClient provideOkHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    public List<Interceptor> provideInterceptors() {
        List<Interceptor> interceptors = new ArrayList<>();
        // add header interceptor
        interceptors.add(getHeaderInterceptor());

        // add logging interceptor
        if (Config.DEBUG) {
            interceptors.add(getLoggingInterceptor());
        }

        return interceptors;
    }

    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                Map<String, String> headers = getHeadersAfterAnnotatedSkip(chain.request().headers());
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    builder.addHeader(header.getKey(), header.getValue());
                }

                return chain.proceed(builder.build());
            }
        };
    }

    private Map<String, String> getHeadersAfterAnnotatedSkip(Headers annotatedHeaders) {
        Map<String, String> configHeaders = new HashMap<>(Config.API_HEADERS);

        for (String headerName : annotatedHeaders.names()) {
            configHeaders.remove(headerName);
        }

        return configHeaders;
    }

    private Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

}