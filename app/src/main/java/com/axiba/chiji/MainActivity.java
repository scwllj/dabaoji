package com.axiba.chiji;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.axiba.chiji.receiver.MyJpushReceiver;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

import static com.tencent.smtt.export.external.interfaces.IX5WebSettings.LOAD_DEFAULT;
import static com.tencent.smtt.export.external.interfaces.IX5WebSettings.LOAD_NO_CACHE;
import static com.tencent.smtt.export.external.interfaces.IX5WebViewBase.HitTestResult.IMAGE_TYPE;
import static com.tencent.smtt.export.external.interfaces.IX5WebViewBase.HitTestResult.SRC_IMAGE_ANCHOR_TYPE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView refresh, home, back, forward, clear, phone_mode, online_server, professional_page;
    private AppCompatImageView refreshImg, homeImg, backImg, clearImg, forwardImg;
    private LinearLayout refreshLayout, homeLayout, backLayout, clearLayout, errorNotice, forwardLayout, contact_us;
    private DrawerLayout drawerLayout;
    private WebView myWebview;
    private RelativeLayout sliderContent, topNavigation;
    private ProgressBar progressBar;
    private FrameLayout sliderMenuContainer;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean showProgressBar;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_PHONE_STATE"};

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage5;

    private final int CODE_UPLOAD_FILE_SINGLE = 213;
    private final int CODE_UPLOAD_FILE_MULT = 11;

    private View xCustomView;
    private IX5WebChromeClient.CustomViewCallback xCustomViewCallback;
    private FrameLayout fullScreenVedio;

    private boolean errorLoaded;
    private PopupMenu popupMenu;

    private Activity instance = this;
    private int screenConfig;
//    BaseConstant baseConstant = new Constant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.FULL_SCREEN) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        MyJpushReceiver.active = true;

        refresh = findViewById(R.id.refresh);
        if (refresh != null) refresh.setOnClickListener(this);
        home = findViewById(R.id.home);
        if (home != null) home.setOnClickListener(this);
        back = findViewById(R.id.back);
        if (back != null) back.setOnClickListener(this);
        forward = findViewById(R.id.forward);
        if (forward != null) forward.setOnClickListener(this);
        clear = findViewById(R.id.clear);
        if (clear != null) clear.setOnClickListener(this);

        contact_us = findViewById(R.id.contact_us);
        if (contact_us != null) contact_us.setOnClickListener(this);
        phone_mode = findViewById(R.id.phone_mode);
        if (phone_mode != null) phone_mode.setOnClickListener(this);
        online_server = findViewById(R.id.online_server);
        if (online_server != null) online_server.setOnClickListener(this);
        professional_page = findViewById(R.id.professional_page);
        if (professional_page != null) professional_page.setOnClickListener(this);


        refreshImg = findViewById(R.id.refresh_img);
        if (refreshImg != null) refreshImg.setOnClickListener(this);
        homeImg = findViewById(R.id.home_img);
        if (homeImg != null) homeImg.setOnClickListener(this);
        backImg = findViewById(R.id.back_img);
        if (backImg != null) backImg.setOnClickListener(this);
        forwardImg = findViewById(R.id.forward_img);
        if (forwardImg != null) forwardImg.setOnClickListener(this);

        refreshLayout = findViewById(R.id.refresh_layout);
        if (refreshLayout != null) refreshLayout.setOnClickListener(this);
        homeLayout = findViewById(R.id.home_layout);
        if (homeLayout != null) homeLayout.setOnClickListener(this);
        backLayout = findViewById(R.id.back_layout);
        if (backLayout != null) backLayout.setOnClickListener(this);
        forwardLayout = findViewById(R.id.forward_layout);
        if (forwardLayout != null) forwardLayout.setOnClickListener(this);
        clearLayout = findViewById(R.id.clear_layout);
        if (clearLayout != null) clearLayout.setOnClickListener(this);
        clearImg = findViewById(R.id.clear_img);
        if (clearImg != null) clearImg.setOnClickListener(this);

        errorNotice = findViewById(R.id.errorNotice);
        if (errorNotice != null) errorNotice.setOnClickListener(this);

        fullScreenVedio = findViewById(R.id.fullScreenVedio);
        myWebview = findViewById(R.id.webview);
        drawerLayout = findViewById(R.id.drawerLayout);
        sliderContent = findViewById(R.id.slider_content);
        progressBar = findViewById(R.id.progressBar);
        topNavigation = findViewById(R.id.top_navi);
        sliderMenuContainer = findViewById(R.id.slider_menu_container);
        swipeRefreshLayout = findViewById(R.id.refesh_layout);
        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return myWebview.getView().getScrollY() > 0;
            }
        });
        myWebview.loadUrl(Constant.START_URL);
        initByConfig();
        initWebview();
        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
        }
        screenConfig = getRequestedOrientation();
        getContentResolver().registerContentObserver(Settings.System
                        .getUriFor(Settings.System.ACCELEROMETER_ROTATION), false,
                oritationObserver);
        if(!isScreenChangeOepn())setRequestedOrientation(instance.getResources().getConfiguration().orientation);

        if(BuildConfig.DEBUG){
            Set<String> tags = new HashSet<>();
            tags.add("test");
            JPushInterface.setTags(this.getApplicationContext(),1,tags);
        }
    }

    ContentObserver oritationObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if(!isScreenChangeOepn()){
                if(instance.getResources().getConfiguration().orientation != 1){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }else{
                setRequestedOrientation(screenConfig);
            }
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
    };

    @Override
    protected void onDestroy() {
        getContentResolver().unregisterContentObserver(oritationObserver);
        super.onDestroy();
    }

    private boolean isScreenChangeOepn(){
        try {
            int screenchange = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
            return screenchange==1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QbSdk.initX5Environment(this.getApplicationContext(),null);
    }

    private void initByConfig() {
        progressBar.setMax(100);
        showProgressBar = Constant.SHOW_PROGRESSBAR;
        progressBar.setVisibility(showProgressBar?View.VISIBLE:View.INVISIBLE);

        swipeRefreshLayout.setEnabled(Constant.PULL_REFRESH);
        if (Constant.PULL_REFRESH) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    myWebview.reload();
                }
            });
        }
    }


    private void initWebview() {
        myWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        myWebview.getSettings().setSupportMultipleWindows(false);
        myWebview.getSettings().setJavaScriptEnabled(true);
        myWebview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        myWebview.getSettings().setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= 16) {
            myWebview.getSettings().setAllowFileAccessFromFileURLs(true);
            myWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        myWebview.getSettings().setAllowFileAccess(true);
        myWebview.getSettings().setAllowContentAccess(true);
        myWebview.getSettings().setBuiltInZoomControls(true);
        myWebview.getSettings().setDisplayZoomControls(false);
        myWebview.getSettings().setLoadsImagesAutomatically(true);
        myWebview.getSettings().setBlockNetworkImage(false);
        myWebview.getSettings().setUseWideViewPort(true);
        myWebview.getSettings().setLoadWithOverviewMode(true);
        myWebview.getSettings().setAppCacheEnabled(true);
        myWebview.getSettings().setCacheMode(Constant.NEED_CACHE ? LOAD_DEFAULT : LOAD_NO_CACHE);
        myWebview.getSettings().setDomStorageEnabled(true);
        myWebview.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            myWebview.getSettings().setMixedContentMode(0);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            myWebview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        myWebview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult result = myWebview.getHitTestResult();
                if ((result.getType() == IMAGE_TYPE) || (result.getType() == SRC_IMAGE_ANCHOR_TYPE)) {
                    instance.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(instance).setMessage("保存图片到相册?")
                                    .setNegativeButton("暂不", null)
                                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (ActivityCompat.checkSelfPermission(instance, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                                                ActivityCompat.requestPermissions(instance, MainActivity.PERMISSIONS_STORAGE, 1);
                                                return;
                                            }
                                            if (result.getExtra().startsWith("http")) {
                                                FileUtils.savePicture(instance, "" + System.currentTimeMillis(), result.getExtra());
                                            } else {
                                                byte[] mBitmap = Base64.decode(result.getExtra(), Base64.DEFAULT);
                                                FileUtils.savaFileToSD(instance, "" + System.currentTimeMillis(), mBitmap);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            }
                                        }
                                    }).show();
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        myWebview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(paramAnonymousString1));
                instance.startActivity(intent);
            }
        });
        myWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("MainActivity", "---shouldOverrideUrlLoading:" + url);
                if (Constant.outerLink != null && Constant.outerLink.get(url) != null) {
                    openSystemBrower(Constant.outerLink.get(url));
                    return true;
                }
                try {
                    if (url.toLowerCase().startsWith("about:blank")) {
                        myWebview.goBack();
                        return true;
                    } else if (url.toLowerCase().startsWith("intent://")) {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    } else if (url.toLowerCase().contains("https://qr.alipay.com")) {
                        int index = url.toLowerCase().indexOf("https://qr.alipay.com");
                        String newUrl = url.substring(index);
                        return super.shouldOverrideUrlLoading(view, newUrl);
                    } else if (!url.toLowerCase().startsWith("http")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                } catch (Exception e) {
//                    Toast.makeText(BaseActivity.this, "无法打开指定应用，请先确认应用是否安装！", Toast.LENGTH_SHORT).show();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }


//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                LogUtil.d("MainActivity", "---shouldOverrideUrlLoading:" + request);
//                return super.shouldOverrideUrlLoading(view, request);
//            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                errorLoaded = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.d("MainActivity", "---onPageFinished");
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (!url.toLowerCase().contains(Constant.HOME_URL)) { //过滤广告
                    if (!hasAd(instance, url)) {
                        return super.shouldInterceptRequest(view, url);
                    } else {
                        return new WebResourceResponse(null, null, null);
                    }
                } else {

                    return super.shouldInterceptRequest(view, url);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (!url.toLowerCase().contains(Constant.HOME_URL)) { //过滤广告
                    if (!hasAd(instance, url)) {
                        return super.shouldInterceptRequest(view, request);
                    } else {
                        return new WebResourceResponse(null, null, null);
                    }
                } else {
                    return super.shouldInterceptRequest(view, request);
                }
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.d("MainActivity", "---onReceivedError" + errorCode + "__" + description);
                if (errorCode == -2) {
                    errorNotice.setVisibility(View.VISIBLE);
                    errorLoaded = true;
                }
            }


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if (errorResponse.getStatusCode() == 404 && request.getUrl().toString().equals(Constant.START_URL)) {
                    errorNotice.setVisibility(View.VISIBLE);
                    errorLoaded = true;
                } else if (errorResponse.getStatusCode() == 500) {
                    errorNotice.setVisibility(View.VISIBLE);
                    errorLoaded = true;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (error.getErrorCode() == -2) {
                    errorNotice.setVisibility(View.VISIBLE);
                    errorLoaded = true;
                }
            }

        });

        myWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar != null) {
                    progressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        errorNotice.setVisibility(errorLoaded ? View.VISIBLE : View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        if (Constant.PULL_REFRESH) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } else {
                        if (showProgressBar) progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                LogUtil.d("MainActivity", "---onCreateWindow");
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                LogUtil.d("MainActivity", "---onShowCustomView");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                myWebview.setVisibility(View.GONE);
                if (xCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                fullScreenVedio.addView(view);
                xCustomView = view;
                xCustomViewCallback = callback;
                fullScreenVedio.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                hideCustomView();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(instance).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                }).show();
                return true;
            }

            @TargetApi(21)
            public boolean onShowFileChooser(WebView paramAnonymousWebView, ValueCallback<Uri[]> paramAnonymousValueCallback, FileChooserParams paramAnonymousFileChooserParams) {
                if (mUploadMessage5 != null) {
                    mUploadMessage5.onReceiveValue(new Uri[]{});
                }
                mUploadMessage5 = paramAnonymousValueCallback;
                Intent intent = paramAnonymousFileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, CODE_UPLOAD_FILE_MULT);
                    return true;
                } catch (ActivityNotFoundException e) {
                    mUploadMessage5.onReceiveValue(new Uri[]{});
                    mUploadMessage5 = null;
                }
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> paramAnonymousValueCallback) {
                openFileChooser(paramAnonymousValueCallback, "*/*");
            }

            public void openFileChooser(ValueCallback<Uri> paramAnonymousValueCallback, String paramAnonymousString) {
                openFileChooser(paramAnonymousValueCallback, paramAnonymousString, null);
            }

            public void openFileChooser(ValueCallback<Uri> paramAnonymousValueCallback, String paramAnonymousString1, String paramAnonymousString2) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = paramAnonymousValueCallback;
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.addCategory("android.intent.category.OPENABLE");
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), CODE_UPLOAD_FILE_SINGLE);
            }
        });
    }

    private void hideCustomView() {
        if (xCustomView == null)
            return;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().clearFlags(1024);
        xCustomView.setVisibility(View.GONE);
        fullScreenVedio.removeView(xCustomView);
        xCustomView = null;
        fullScreenVedio.setVisibility(View.INVISIBLE);
        xCustomViewCallback.onCustomViewHidden();
        myWebview.setVisibility(View.VISIBLE);
    }

    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.toLowerCase().contains(adUrl)) {
                return true;
            }
        }
        return false;
    }

    private void openSystemBrower(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

        if (requestCode == CODE_UPLOAD_FILE_MULT) {
            if (result != null && mUploadMessage5 != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mUploadMessage5.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                } else {
                    mUploadMessage5.onReceiveValue(new Uri[]{result});
                }
            } else {
                if (mUploadMessage5 != null) {
                    mUploadMessage5.onReceiveValue(new Uri[]{});
                }
            }
        } else if (requestCode == CODE_UPLOAD_FILE_SINGLE) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
            }
        }
        if (mUploadMessage5 != null) {
            mUploadMessage5 = null;
        }
        if (mUploadMessage != null) {
            mUploadMessage = null;
        }
    }

    @Override
    public void onClick(View v) {

        if (v == home || v == homeLayout || v == homeImg) {
            myWebview.loadUrl(Constant.HOME_URL);
        } else if (v == refresh || v == refreshLayout || v == refreshImg) {
            myWebview.reload();
        } else if (v == back || v == backLayout || v == backImg) {
            if (myWebview.canGoBack()) {
                myWebview.goBack();
            }
        } else if (v == forward || v == forwardLayout || v == forwardImg) {
            if (myWebview.canGoForward()) {
                myWebview.goForward();
            }
        } else if (v == clear || v == clearLayout || v == clearImg) {
            clearCache();
        } else if (v == errorNotice) {
            myWebview.reload();
        } else if (v == contact_us) {
            showPopMenu(v);
        }
    }

    void showPopMenu(View view) {
        PopView popView = new PopView(this, view);
        popView.setData(Constant.popMenu);
        popView.setOnItemClickListener(new PopView.OnItemClickListener() {
            @Override
            public void onItemClick(PopView.Menu menu) {
                myWebview.loadUrl(menu.getValue());
            }
        });
        popView.show();
    }

    private void clearCache() {
        new android.app.AlertDialog.Builder(this).setMessage("确认需要清理缓存？")
                .setNegativeButton("取消", null)
                .setPositiveButton("清理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearWebViewCache();
                        Toast.makeText(instance, "已成功清理缓存", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    public void clearWebViewCache() {
        //清理Webview缓存数据库
        try {
            myWebview.clearCache(true);
            myWebview.clearFormData();
            File file = getCacheDir();
            if ((file != null && file.exists()) && file.isDirectory()) {
                for (File item : file.listFiles()) {
                    item.delete();
                }
                file.delete();
            }
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            Log.e("----clearWebViewCache", "" + e.getMessage());
        }
    }


    long mills = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (xCustomView != null) {
                hideCustomView();
                return true;
            }
            if (myWebview.canGoBack()) {
                myWebview.goBack();
                return true;
            }
            if (System.currentTimeMillis() - mills > 1000) {
                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                mills = System.currentTimeMillis();
            } else {
                MyJpushReceiver.active = false;
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
