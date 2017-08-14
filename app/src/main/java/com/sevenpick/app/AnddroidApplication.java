package com.sevenpick.app;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Created by Administrator on 2017/8/14.
 */

public class AnddroidApplication extends Application {
    private static final String APPID="b3653b8488";

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), APPID, false);
    }
}
