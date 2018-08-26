package com.axiba.chiji;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.tencent.smtt.sdk.WebView;

public class MyWebview extends WebView {

    public MyWebview(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public MyWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
