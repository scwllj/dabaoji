package com.axiba.chiji;

import android.util.Log;


public class LogUtil {
    public static void d(String tag,String content){
        if(BuildConfig.DEBUG){
            Log.d(tag,content);
        }
    }
}
