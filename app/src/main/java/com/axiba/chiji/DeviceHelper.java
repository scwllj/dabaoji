package com.axiba.chiji;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceHelper {

    public static float density;
    public static int screenW;
    public static int screenH;
    private static int statusBarHeight;

    static {
        init();
    }

    public static int dp2px(int dip) {
        return (int) (density * dip + 0.5);
    }

    public static void init() {
        WindowManager windowManager =
                (WindowManager) SharedApplication.instance.getSystemService(Context.
                        WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealMetrics(dm);
        } else {
            // 不可能有虚拟按键
            display.getMetrics(dm);
        }
        density = dm.density;
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;
    }

    public static int getStatusBarHeight(){
        if(statusBarHeight == 0){
            int resourceId = SharedApplication.instance.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = SharedApplication.instance.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }
}
