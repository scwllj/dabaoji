package com.axiba.chiji;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatTextView refresh,home,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refresh = findViewById(R.id.refresh);
        if(refresh!=null)refresh.setOnClickListener(this);
        home = findViewById(R.id.home);
        if(home!=null)home.setOnClickListener(this);
        back = findViewById(R.id.refresh);
        if(back!=null)back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

    }
}
