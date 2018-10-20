package com.axiba.chiji;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

import java.util.Map;

public class MyWebview extends WebView {


    public MyWebview(Context context) {
        super(context);
    }

    public MyWebview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyWebview(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MyWebview(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
    }

    public MyWebview(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
    }
}
