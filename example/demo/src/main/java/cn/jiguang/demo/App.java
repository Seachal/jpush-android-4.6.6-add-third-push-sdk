package cn.jiguang.demo;


import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;

import cn.jpush.android.api.JPushInterface;


/**
 * Copyright(c) 2020 极光
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initToast();
        initKV();

        // 初始化SDK
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initKV() {
        MMKV.initialize(this);
    }

    private void initToast() {
        ToastUtils.init(this);
    }
}
