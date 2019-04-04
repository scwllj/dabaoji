package com.axiba.chiji;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.QbSdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Loading extends AppCompatActivity {

    private static final String TAG = "LOADING";
    private Bitmap bitmap;
    CircleProgress circleProgress;
    ViewPager viewpager;
    List<Bitmap> cache = new ArrayList<>();
    List<View> views = new ArrayList<>();
    final String FOLDER_NAME = "guide";
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"};
    private boolean granted;
    private boolean endAnim;
    BaseConstant baseConstant = new Constant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loading);
        viewpager = findViewById(R.id.viewpager);
        granted = checkPermission();
        if (baseConstant.showGuideDirectly()) {
            showGuideOrToMainDirectly();
        } else {
            showLoading();
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        granted = true;
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.initX5Environment(this.getApplicationContext(), new QbSdk.PreInitCallback() {
                @Override
                public void onCoreInitFinished() {
                    Log.d(TAG, "----onCoreInitFinished: ");
                }

                @Override
                public void onViewInitFinished(boolean b) {
                    Log.d(TAG, "----onViewInitFinished: " + b);
//                    showGuideOrToMain();
                }
            });
            showGuideOrToMain();
        } else {
            showGuideOrToMain();
        }


    }

    private void showLoading() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.loading, options);
        while (options.outHeight > 2500) {
            options.inSampleSize += 1;
            BitmapFactory.decodeResource(getResources(), R.drawable.loading, options);
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading, options);
        ((ImageView) findViewById(R.id.loading_image)).setImageBitmap(bitmap);
        circleProgress = findViewById(R.id.circle_progress);
        circleProgress.setMaxProgress(1000);
        final ValueAnimator animator = ValueAnimator.ofInt(0, 1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000);
        circleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.end();
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                circleProgress.setProgress(value);

            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                endAnim = true;
                showGuideOrToMain();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeResource(getResources(),R.drawable.loading,options);
//            while (options.outHeight>2500){
//                options.inSampleSize+=1;
//                BitmapFactory.decodeResource(getResources(),R.drawable.loading,options);
//            }
//            options.inJustDecodeBounds = false;
//            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading,options);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


    private void showGuideOrToMain() {
        if (!granted || !endAnim) {
            return;
        }
        showGuideOrToMainDirectly();
    }

    private void showGuideOrToMainDirectly() {
        try {
            String[] files = getAssets().list(FOLDER_NAME);
            if ((files == null
                    || files.length == 0)
//                    || !getSharedPreferences("config", MODE_PRIVATE).getBoolean("firstInstalled", true)
                    ) {
                startActivity(new Intent(Loading.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            } else {
                getSharedPreferences("config", MODE_PRIVATE).edit().putBoolean("firstInstalled", false).commit();
                Arrays.sort(files);
                for (int i = 0; i < files.length; i++) {
                    View view;
                    cache.add(getImageFromAssetsFile(FOLDER_NAME + "/" + files[i]));
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(cache.get(i));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (i == files.length - 1) {
//                        FrameLayout frameLayout = new FrameLayout(this);
//                        frameLayout.addView(imageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//                        TextView textView = new TextView(this);
//                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                        params.gravity = Gravity.RIGHT;
//                        params.rightMargin = 30;
//                        params.topMargin = 30;
//                        textView.setText("进入主页");
//                        textView.setPadding(20, 10, 20, 10);
//                        textView.setBackgroundResource(R.drawable.to_main_bg);
//                        frameLayout.addView(textView, params);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Loading.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }
                        });
                        view = imageView;
                    } else {
                        view = imageView;
                    }
                    views.add(view);
                }
                initPager();
                viewpager.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initPager() {
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return cache.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("addOnPageChangeListener", positionOffset + "onPageScrolled: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
