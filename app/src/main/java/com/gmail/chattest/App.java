package com.gmail.chattest;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        MultiDex.install(getBaseContext());
    }

    public static Context getContext(){
        return context;
    }
}
