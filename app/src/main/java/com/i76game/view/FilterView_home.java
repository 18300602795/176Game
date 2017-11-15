package com.i76game.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.i76game.R;
import com.i76game.model.FilterEntity;
import com.i76game.model.FilterTwoEntity;


/**
 * Created by sunfusheng on 16/4/20.
 */
public class FilterView_home extends LinearLayout implements View.OnClickListener {


    private Context context;

    private int filterPosition = -1;
    private int lastFilterPosition = -1;

    private boolean isShowing = false;


    public FilterView_home(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public FilterView_home(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_layout_home, this);
        initView();
        initListener();
    }

    public void clear() {
    }


    private void initView() {
//        viewMaskBg.setVisibility(GONE);
    }

    private void initListener() {
//        viewMaskBg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    }

    // 复位筛选的显示状态
    public void resetViewStatus() {
    }

    // 复位所有的状态
    public void resetAllStatus() {
        resetViewStatus();
        hide();
    }


    // 动画显示
    public void show(int position) {
        if (isShowing && lastFilterPosition == position) {
            hide();
            return;
        } else if (!isShowing) {
        }
        isShowing = true;
        resetViewStatus();
        lastFilterPosition = position;

    }

    // 隐藏动画
    public void hide() {
        isShowing = false;
        resetViewStatus();
        filterPosition = -1;
        lastFilterPosition = -1;
    }

    // 是否显示
    public boolean isShowing() {
        return isShowing;
    }


    // 筛选视图点击
    private OnFilterClickListener onFilterClickListener;

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }

    // 分类Item点击
    private OnItemCategoryClickListener onItemCategoryClickListener;

    public void setOnItemCategoryClickListener(OnItemCategoryClickListener onItemCategoryClickListener) {
        this.onItemCategoryClickListener = onItemCategoryClickListener;
    }

    public interface OnItemCategoryClickListener {
        void onItemCategoryClick(FilterTwoEntity leftEntity, FilterEntity rightEntity);
    }

    // 排序Item点击
    private OnItemSortClickListener onItemSortClickListener;

    public void setOnItemSortClickListener(OnItemSortClickListener onItemSortClickListener) {
        this.onItemSortClickListener = onItemSortClickListener;
    }

    public interface OnItemSortClickListener {
        void onItemSortClick(FilterEntity entity);
    }

    // 筛选Item点击
    private OnItemFilterClickListener onItemFilterClickListener;

    public void setOnItemFilterClickListener(OnItemFilterClickListener onItemFilterClickListener) {
        this.onItemFilterClickListener = onItemFilterClickListener;
    }

    public interface OnItemFilterClickListener {
        void onItemFilterClick(FilterEntity entity);
    }

}
