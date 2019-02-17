package com.gmail.chattest.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.gmail.chattest.R;
import com.gmail.chattest.common.NavigationHelper;
import com.gmail.chattest.root.BaseActivity;

import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NavigationHelper.cloud(this);

        //NavigationHelper.next(getBaseContext());

        /*Observable.timer( 5 , TimeUnit.SECONDS )
        .subscribe( data ->
            NavigationHelper.next(getBaseContext()));
        */
    }
}
