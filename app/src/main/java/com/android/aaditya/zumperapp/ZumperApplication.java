package com.android.aaditya.zumperapp;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by aaditya on 11/11/17.
 */

public class ZumperApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}

