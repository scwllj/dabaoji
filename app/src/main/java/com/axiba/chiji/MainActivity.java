package com.axiba.chiji;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.webkit.WebSettings.LOAD_DEFAULT;
import static android.webkit.WebSettings.LOAD_NO_CACHE;
import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static android.webkit.WebView.HitTestResult.IMAGE_TYPE;
import static android.webkit.WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatTextView refresh,home,back,errorNotice,forward;
    private DrawerLayout drawerLayout;
    private WebView myWebview;
    private RelativeLayout sliderContent,topNavigation;
    private ProgressBar progressBar;
    private FrameLayout sliderMenuContainer;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static String[] PERMISSIONS_STORAGE = { "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE" };

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage5;

    private final int CODE_UPLOAD_FILE_SINGLE = 213;
    private final int CODE_UPLOAD_FILE_MULT = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getBoolean(R.bool.full_screen)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        refresh = findViewById(R.id.refresh);
        if(refresh!=null)refresh.setOnClickListener(this);
        home = findViewById(R.id.home);
        if(home!=null)home.setOnClickListener(this);
        back = findViewById(R.id.back);
        if(back!=null)back.setOnClickListener(this);
        forward = findViewById(R.id.forward);
        if(forward!=null)forward.setOnClickListener(this);
        errorNotice = findViewById(R.id.errorNotice);
        if(errorNotice!=null)errorNotice.setOnClickListener(this);

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
                return myWebview.getScrollY()>0;
            }
        });
        myWebview.loadUrl(getResources().getString(R.string.start_url));
        initByConfig();
        initWebview();
        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
        }
    }

    private void initByConfig() {
        progressBar.setMax(100);

        if(getResources().getBoolean(R.bool.need_back)&&back!=null)back.setVisibility(View.VISIBLE);
        switch (getResources().getInteger(R.integer.right_action)){
            case 1:
                if(home!=null)home.setVisibility(View.VISIBLE);
                break;
            case 2:
                if(refresh!=null)refresh.setVisibility(View.VISIBLE);
                break;
        }

        topNavigation.setVisibility(getResources().getBoolean(R.bool.save_daohang)?View.VISIBLE:View.GONE);
        final boolean refreshable;
        swipeRefreshLayout.setEnabled(refreshable=getResources().getBoolean(R.bool.pull_refresh_enable));
        if(refreshable){
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    myWebview.reload();
                }
            });
        }
    }


    private void initWebview(){
        myWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        myWebview.getSettings().setSupportMultipleWindows(false);
        myWebview.getSettings().setJavaScriptEnabled(true);
        myWebview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        myWebview.getSettings().setSupportZoom(true);
        if(Build.VERSION.SDK_INT>=16){
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
        myWebview.getSettings().setCacheMode(getResources().getBoolean(R.bool.need_cache)? LOAD_DEFAULT:LOAD_NO_CACHE);
        myWebview.getSettings().setDomStorageEnabled(true);
        myWebview.getSettings().setAppCacheEnabled(true);
        myWebview.getSettings().setUserAgentString(myWebview.getSettings().getUserAgentString()+" androidapp");
        if (Build.VERSION.SDK_INT >= 21) {
            myWebview.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            myWebview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        myWebview.addJavascriptInterface(new AndroidJs(),"AndroidJs");//彩宝
//        myWebview.addJavascriptInterface(new AndroidJs(),"App9vCom");


        myWebview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult result = myWebview.getHitTestResult();
                if ((result.getType() == IMAGE_TYPE) || (result.getType() == SRC_IMAGE_ANCHOR_TYPE))
                {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(MainActivity.this).setMessage("保存图片到相册?")
                                    .setNegativeButton("暂不",null)
                                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0)
                                            {
                                                ActivityCompat.requestPermissions(MainActivity.this, MainActivity.PERMISSIONS_STORAGE, 1);
                                                return;
                                            }
                                            if(result.getExtra().startsWith("http")){
                                                FileUtils.savePicture(MainActivity.this,""+System.currentTimeMillis(),result.getExtra());
                                            }else{
                                                byte[] mBitmap = Base64.decode(result.getExtra(), Base64.DEFAULT);
                                                FileUtils.savaFileToSD(MainActivity.this,""+System.currentTimeMillis(),mBitmap);
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
        myWebview.setDownloadListener(new DownloadListener()
        {
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong)
            {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(paramAnonymousString1));
                MainActivity.this.startActivity(intent);
            }
        });
        final String outer = "https://wpa.qq.com/msgrd";
        myWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(getResources().getBoolean(R.bool.outer_web)){
                    String yuming = getResources().getString(R.string.home_url).split("/")[2];
                    if(url.contains(yuming)){
                        return super.shouldOverrideUrlLoading(view,url);
                    }
                    /*直接跳系统浏览器 */
                    openSystemBrower(url);
                    return true;
                    /*---------*/
                }
//                if(url.contains(outer)){
//                    openSystemBrower(url);
//                    return true;
//                }
                Log.d("MainActivity", "---shouldOverrideUrlLoading: "+url);
                if(url.endsWith(".mp4")){
                    return true;
                }

                try {
                    if (url.toLowerCase().startsWith("intent://")) {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    }
                    else if (url.toLowerCase().contains("https://qr.alipay.com")) {
                        int index = url.toLowerCase().indexOf("https://qr.alipay.com");
                        String newUrl = url.substring(index);
                        view.loadUrl(newUrl);
                        return true;
                    }
                    else if (!url.toLowerCase().startsWith("http")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view,url);
                } catch (Exception e) {
//                    Toast.makeText(BaseActivity.this, "无法打开指定应用，请先确认应用是否安装！", Toast.LENGTH_SHORT).show();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                errorNotice.setVisibility(View.INVISIBLE);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.toLowerCase().contains(".swf") || url.toLowerCase().contains(".mp4")) {
                    return new WebResourceResponse(null, null, null);
                }
                if (!url.toLowerCase().contains(getString(R.string.home_url))) { //过滤广告
                    if (!hasAd(MainActivity.this, url)) {
                        return super.shouldInterceptRequest(view, url);
                    } else {
                        return new WebResourceResponse(null, null, null);
                    }
                } else {

                    return super.shouldInterceptRequest(view, url);
                }
            }
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtil.d("--MainActivity", "onReceivedError:" + error);
            }

        });
        myWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar != null) {
                    progressBar.setProgress(newProgress);
                    if(newProgress==100){
                        progressBar.setVisibility(View.INVISIBLE);
                        if (getResources().getBoolean(R.bool.pull_refresh_enable)) {
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                result.confirm();
                return true;
//                return super.onJsAlert(view, url, message, result);
            }

            @TargetApi(21)
            public boolean onShowFileChooser(WebView paramAnonymousWebView, ValueCallback<Uri[]> paramAnonymousValueCallback, WebChromeClient.FileChooserParams paramAnonymousFileChooserParams)
            {
                if (MainActivity.this.mUploadMessage5 != null)
                {
                    MainActivity.this.mUploadMessage5.onReceiveValue(null);
                }
                mUploadMessage5 = paramAnonymousValueCallback;
                Intent intent = paramAnonymousFileChooserParams.createIntent();
                try
                {
                    MainActivity.this.startActivityForResult(intent, CODE_UPLOAD_FILE_MULT);
                    return true;
                }
                catch (ActivityNotFoundException e)
                {

                }
                return false;
            }

            public void openFileChooser(ValueCallback<Uri> paramAnonymousValueCallback)
            {
                openFileChooser(paramAnonymousValueCallback, "*/*");
            }

            public void openFileChooser(ValueCallback<Uri> paramAnonymousValueCallback, String paramAnonymousString)
            {
                openFileChooser(paramAnonymousValueCallback, paramAnonymousString, null);
            }

            public void openFileChooser(ValueCallback<Uri> paramAnonymousValueCallback, String paramAnonymousString1, String paramAnonymousString2)
            {
                mUploadMessage = paramAnonymousValueCallback;
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.addCategory("android.intent.category.OPENABLE");
                intent.setType("*/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(intent, "File Browser"), CODE_UPLOAD_FILE_SINGLE);
            }
        });
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

    private void openSystemBrower(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
        if(result!=null){
            if(requestCode==CODE_UPLOAD_FILE_MULT){
                    if (mUploadMessage5 != null) {
                        mUploadMessage5.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data));
                    }
                    mUploadMessage5 = null;
            }
        }else if (requestCode==CODE_UPLOAD_FILE_SINGLE){
            if(mUploadMessage!=null){
//                String path = getRealPathByUri(BaseActivity.this,result);
//                mUploadMessage.onReceiveValue(path==null?null:Uri.fromFile(new File(path)));
                mUploadMessage.onReceiveValue(result);
            }
            mUploadMessage = null;
        }

    }

    @Override
    public void onClick(View v) {
        if(v==home){
            myWebview.loadUrl(getResources().getString(R.string.home_url));
        }else if (v==refresh){
            myWebview.reload();
        }else if (v==back){
            if(myWebview.canGoBack()){
                myWebview.goBack();
            }
        }else if (v==back){
            if(myWebview.canGoForward()){
                myWebview.goForward();
            }
        }
        else if (v==errorNotice){
            errorNotice.setVisibility(View.GONE);
            myWebview.reload();
        }
    }

    long mills = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myWebview.canGoBack()) {
                myWebview.goBack();
                return true;
            }
            if (System.currentTimeMillis() - mills > 1000) {
                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                mills = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class AndroidJs {
        @JavascriptInterface
        public void share(){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Share.shareWebLinkWithIcon(MainActivity.this,myWebview.getTitle()+"\n"+myWebview.getUrl());
                }
            });
        }

        @JavascriptInterface
        public void OpenWithSafari(final String url){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    openSystemBrower(url);
                }
            });
        }

    }

}
