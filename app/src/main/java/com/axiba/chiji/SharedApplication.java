package com.axiba.chiji;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class SharedApplication extends Application {
    private final String TAG = this.getClass().getName();

    public static volatile SharedApplication instance;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.drawable.iconx;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  // 设置为铃声与震动都要
        JPushInterface.setPushNotificationBuilder(1, builder);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this.getApplicationContext());
        initX5();
        DeviceHelper.init();
        UMConfigure.init(this, null, null, 1, null);
    }

    private void initX5() {
        QbSdk.initX5Environment(this.getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                LogUtil.d(TAG, "----onCoreInitFinished: ");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtil.d(TAG, "----onViewInitFinished: " + b);
            }
        });
    }

}
