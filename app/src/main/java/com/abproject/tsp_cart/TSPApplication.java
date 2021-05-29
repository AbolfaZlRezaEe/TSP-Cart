package com.abproject.tsp_cart;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class TSPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Timber for Project.
        Timber.plant(new Timber.DebugTree());
    }
}
