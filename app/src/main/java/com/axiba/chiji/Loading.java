package com.axiba.chiji;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class Loading extends Activity {

    private Bitmap bitmap;
    CircleProgress circleProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                startActivity(new Intent(Loading.this,MainActivity.class));
                finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
        }
    }
}
