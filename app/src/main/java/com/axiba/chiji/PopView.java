package com.axiba.chiji;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class PopView extends FrameLayout {

    private View anchorView;
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private FrameLayout contentParent;
    private int cellHeight = 30;
    private int screenWidth,screenHeight;
    private int cellWidth = 100;
    private final int HORIZONTAL_MIN = 40;
    private float density;
    private int allHeight;



    public PopView(@NonNull Context context) {
        super(context);
    }

    public PopView(@NonNull Context context,View anchorView) {
        this(context);
        this.anchorView = anchorView;
        contentParent =
                (FrameLayout) anchorView.getRootView().findViewById(android.R.id.content);
        createPopWindow();
    }

    public PopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void createPopWindow(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        density = dm.density;

        cellHeight = Math.round(density*cellHeight);
        cellWidth = Math.round(density*cellWidth);

        recyclerView = new RecyclerView(getContext());
        recyclerView.setBackgroundColor(Color.GREEN);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setPadding(0,Math.round(0*density),0,Math.round(0*density));
        menuAdapter = new MenuAdapter();
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recyclerView.setBackgroundResource(R.drawable.pop_view_bg);
        addView(recyclerView,new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setData(List<Menu> data){
        if(data == null || data.size()==0){
            return;
        }
        int []anchorViewPoint = new int[2];
        anchorView.getLocationOnScreen(anchorViewPoint);
        int anchorWidth = anchorView.getMeasuredWidth();
        int anchorHeight = anchorView.getMeasuredHeight();

        LayoutParams params = (FrameLayout.LayoutParams)recyclerView.getLayoutParams();

        allHeight = cellHeight * data.size()+Math.round(20*density);

        if(anchorViewPoint[1] + anchorHeight + allHeight > screenHeight){
            params.gravity = Gravity.BOTTOM;
            params.bottomMargin = screenHeight-anchorViewPoint[1]+10;
        }else {
            params.topMargin = anchorViewPoint[1] + anchorHeight + HORIZONTAL_MIN;
        }
        if(anchorViewPoint[0] < HORIZONTAL_MIN || anchorViewPoint[0] + anchorWidth/2 < cellWidth/2){
            params.leftMargin = HORIZONTAL_MIN;
        }else if (anchorViewPoint[0] + cellWidth > screenWidth){
            params.gravity = params.gravity | Gravity.RIGHT;
            params.rightMargin = HORIZONTAL_MIN;
        }else {
            params.leftMargin = anchorViewPoint[0] + anchorWidth/2 - cellWidth/2;
        }
        params.height = allHeight;
        params.width = cellWidth;

        recyclerView.setLayoutParams(params);
        menuAdapter.setData(data);

    }

    public void dismiss(){
        ValueAnimator animator = ValueAnimator.ofFloat(1,0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                recyclerView.setAlpha(value);
                recyclerView.setScaleX(value);
                recyclerView.setScaleY(value);
            }
        });
        animator.setDuration(150);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contentParent.removeView(PopView.this);
            }
        });
        animator.start();

    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
       menuAdapter.setOnItemClickListener(new OnItemClickListener() {
           @Override
           public void onItemClick(Menu menu) {
               onItemClickListener.onItemClick(menu);
               dismiss();
           }
       });
    }

    public void show(){
        contentParent.addView(this,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                recyclerView.setAlpha(value);
                recyclerView.setScaleX(value);
                recyclerView.setScaleY(value);
            }
        });
        animator.setDuration(150);
        animator.start();
    }

    public interface OnItemClickListener{
        void onItemClick(Menu menu);
    }

    private static class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {

        List<Menu> data;
        private OnItemClickListener onItemClickListener;

        @NonNull
        @Override
        public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new MenuHolder(getView(parent,R.layout.menu_adapter));
        }

        @Override
        public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
            holder.menuTextView.setText(data.get(position).getName());
            holder.menuTextView.setTag(data.get(position));
            holder.divider.setVisibility(position == data.size()-1?INVISIBLE:VISIBLE);
            holder.menuTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Menu menu = (Menu) v.getTag();
                    onItemClickListener.onItemClick(menu);

                }
            });
        }

        @Override
        public int getItemCount() {
            return data==null?0:data.size();
        }

        public void setData(List<Menu> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        protected View getView(ViewGroup parent, int layoutId) {
            return LayoutInflater.from(parent.getContext()).inflate(layoutId, null, false);
        }
    }

    private static class MenuHolder extends RecyclerView.ViewHolder{

        private TextView menuTextView;
        private View divider;
        public MenuHolder(View itemView) {
            super(itemView);
            menuTextView = itemView.findViewById(R.id.pop_menu_name);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    public static class Menu<T>{
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Menu(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
