package com.example.ienning.ncuhome.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ienning on 16-7-28.
 */
public class MyApplication extends Application{
    private static Context context;
    public static SharedPreferences sharedPre;
    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
