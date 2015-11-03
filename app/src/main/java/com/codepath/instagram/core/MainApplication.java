package com.codepath.instagram.core;

import android.app.Application;

import com.codepath.instagram.helpers.InstagramClient;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication instance;
    private static InstagramClient instagramClient;

    public static MainApplication sharedApplication() {
        assert(instance != null);
        return instance;
    }

    public static InstagramClient getInstagramClient() {
        assert(instagramClient != null);
        return instagramClient;
    }

    @Override
    public void onCreate() {
        instance = this;
        instagramClient = new InstagramClient(this);
        super.onCreate();
        Fresco.initialize(this);
    }
}
