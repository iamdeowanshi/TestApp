package com.android.aaditya.zumperapp;

import android.content.pm.ActivityInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class Config {

    //--------------------------------------------------------------------------------
    // App general configurations
    //--------------------------------------------------------------------------------
    public static final boolean DEBUG = true;

    public static final int ORIENTATION_PORTRAIT    = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public static final int ORIENTATION_LANDSCAPE   = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    public static final int ORIENTATION_SENSOR      = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    public static final int ORIENTATION_DEFAULT     = ORIENTATION_PORTRAIT;

    //--------------------------------------------------------------------------------
    // API related constants/configurations - used in ApiModule
    //--------------------------------------------------------------------------------
    public static final String API_BASE_URL_PRODUCTION = "https://maps.googleapis.com/maps/api/place/";
    public static final String API_BASE_URL_MOCK = "";


    // Active base url
    public static final String API_BASE_URL = API_BASE_URL_PRODUCTION;

    // Common http headers required to be added by interceptor
    public static final Map<String, String> API_HEADERS = new HashMap<String, String>() {{
        put("User-Agent", "Zumper-App");
        put("Content-Type", "application/json");
    }};

    // Key

    public static final String GOOGLE_API_KEY = "AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk";



}
