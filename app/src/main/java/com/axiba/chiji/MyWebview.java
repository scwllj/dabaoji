package com.axiba.chiji;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;


import static android.webkit.WebView.HitTestResult.IMAGE_TYPE;
import static android.webkit.WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE;

public class MyWebview extends WebView {

    private LongClickCallBack longClickCallBack;

    public MyWebview(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public MyWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);
        getSettings().setUseWideViewPort(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setBlockNetworkImage(false);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setCacheMode(getResources().getBoolean(R.bool.need_cache)?WebSettings.LOAD_DEFAULT:WebSettings.LOAD_NO_CACHE);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheEnabled(true);
//        addJavascriptInterface(new JsInterface(), "jsinterface");
        if (Build.VERSION.SDK_INT >= 21) {
            getSettings().setMixedContentMode(0);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HitTestResult paramAnonymousView = getHitTestResult();
                if ((paramAnonymousView.getType() == IMAGE_TYPE) || (paramAnonymousView.getType() == SRC_IMAGE_ANCHOR_TYPE))
                {
                    if(longClickCallBack!=null){
                        longClickCallBack.onSaveImage(paramAnonymousView.getExtra());
                    }
                    return true;
                }
                return false;
            }
        });
        setDownloadListener(new DownloadListener()
        {
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong)
            {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(paramAnonymousString1));
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setLongClickCallBack(LongClickCallBack longClickCallBack) {
        this.longClickCallBack = longClickCallBack;
    }

    public static interface LongClickCallBack{
        void onSaveImage(String content);
    }

}
