package cn.jiguang.union.ads.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import cn.jiguang.api.JCoreInterface;
import cn.jpush.android.api.JPushInterface;

public class MainApplication extends Application {

    private static final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

}
