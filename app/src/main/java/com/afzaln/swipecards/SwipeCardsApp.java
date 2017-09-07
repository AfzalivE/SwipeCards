package com.afzaln.swipecards;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by afzal on 2017-09-07.
 */

public class SwipeCardsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
