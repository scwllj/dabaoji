package com.axiba.chiji;

import android.app.Application;

public class SharedApplication extends Application {

    public static volatile SharedApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
