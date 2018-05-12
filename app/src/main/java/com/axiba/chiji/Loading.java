package com.axiba.chiji;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.io.IOException;

public class Loading extends AppCompatActivity {

    private Bitmap bitmap;
    private CircleProgress circleProgress;
    private ViewPager guidePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loading);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),R.drawable.loading,options);
        while (options.outHeight>2500){
            options.inSampleSize+=1;
            BitmapFactory.decodeResource(getResources(),R.drawable.loading,options);
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading,options);
        ((ImageView)findViewById(R.id.loading_image)).setImageBitmap(bitmap);
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
                SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
                boolean first = sp.getBoolean("first_install",true);
                if(getResources().getBoolean(R.bool.need_guide) && first){
                    loadGuide();
                }else {
                    startActivity(new Intent(Loading.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();


        guidePage = findViewById(R.id.guide_page);


    }

    private void loadGuide(){
        guidePage.setVisibility(View.VISIBLE);
        try {
            final String[] fileList = getAssets().list("guide");

            guidePage.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return fileList.length;
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                    return view==object;
                }

                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    return super.instantiateItem(container, position);
                }

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                    super.destroyItem(container, position, object);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
        }
    }
}
