package com.cworld.notie;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class NotieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
