package com.axiba.chiji;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class PopView extends FrameLayout {

    private View anchorView;
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;

    public PopView(@NonNull Context context) {
        super(context);
    }

    public PopView(@NonNull Context context,View anchorView) {
        this(context);
        this.anchorView = anchorView;
        createPopWindow();
    }

    public PopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void createPopWindow(){
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setOnClickListener(null);
        recyclerView = new RecyclerView(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(menuAdapter);
        addView(recyclerView);
    }

    public void setData(List<Map<String, Object>> data){
        menuAdapter.setData(data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
       menuAdapter.setOnItemClickListener(onItemClickListener);
    }

    public void show(){
        WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(this,new WindowManager.LayoutParams());
    }

    public interface OnItemClickListener{
        void onItemClick(String key,Object value);
    }

    private static class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {

        List<Map<String, Object>> data;
        private OnItemClickListener onItemClickListener;

        @NonNull
        @Override
        public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new MenuHolder(getView(parent,R.layout.menu_adapter));
        }

        @Override
        public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
//            holder
        }

        @Override
        public int getItemCount() {
            return data==null?0:data.size();
        }

        public void setData(List<Map<String, Object>> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        protected View getView(ViewGroup parent, int layoutId) {
            return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }
    }

    private static class MenuHolder extends RecyclerView.ViewHolder{

        private TextView menuTextView;
        public MenuHolder(View itemView) {
            super(itemView);
            menuTextView = itemView.findViewById(R.layout.menu_adapter);
        }
    }
}
