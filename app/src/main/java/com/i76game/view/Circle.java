package com.i76game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;

/**
 * Created by Administrator on 2017/5/15.
 */

public class Circle extends ViewGroup{

    private int mRadius;
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    /**
     * 菜单的中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;

    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private float mPadding;

    /**
     * 布局时的开始角度
     */
    private double mStartAngle = 0;

    public Circle(Context context) {
        this(context,null);
    }

    public Circle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;

        // Laying out the child views
        final int childCount = getChildCount();

        int left, top;
        // menu item 的尺寸
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        // 根据menu item的个数，计算角度
        float angleDelay = 360 / (getChildCount() - 1);

        // 遍历去设置menuitem的位置
        for (int i = 0; i < childCount; i++)
        {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
            {
                continue;
            }

            mStartAngle %= 360;

            // 计算，中心点到menu item中心的距离
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

            // tmp cosa 即menu item中心点的横坐标
            left = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);
            // tmp sina 即menu item的纵坐标
            top = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);

            child.layout(left, top, left + cWidth, top + cWidth);
            // 叠加尺寸
            mStartAngle += angleDelay;
        }

//         找到中心的view，如果存在设置onclick事件
        View cView = findViewById(R.id.circle_center_img);
        if (cView != null)
        {
            cView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (mOnMenuItemClickListener != null)
                    {
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                }
            });
            // 设置center item位置
            int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
            int cr = cl + cView.getMeasuredWidth();
            cView.layout(cl, cl, cr, cr);
        }
    }

    private int[] mItemImgs;
    private String[] mItemTexts;
    private int mMenuItemCount;
    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts)
    {
        mItemImgs = resIds;
        mItemTexts = texts;

        // 参数检查
        if (resIds == null && texts == null)
        {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }

        // 初始化mMenuCount
        mMenuItemCount = resIds == null ? texts.length : resIds.length;

        if (resIds != null && texts != null)
        {
            mMenuItemCount = Math.min(resIds.length, texts.length);
        }

        addMenuItems();

    }

    /**
     * 添加菜单项
     */
    private void addMenuItems()
    {
        LayoutInflater mInflater = LayoutInflater.from(getContext());

        /**
         * 根据用户设置的参数，初始化view
         */
        for (int i = 0; i < mMenuItemCount; i++)
        {
            final int j = i;
            View view = mInflater.inflate(R.layout.circlr_item_layout, this,
                    false);
            ImageView iv = (ImageView) view
                    .findViewById(R.id.circle_img);
            TextView tv = (TextView) view
                    .findViewById(R.id.circle_text);

            if (iv != null)
            {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(mItemImgs[i]);
                iv.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        if (mOnMenuItemClickListener != null)
                        {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }
            if (tv != null)
            {
                tv.setVisibility(View.VISIBLE);
                tv.setTextColor(getResources().getColor(R.color.main_item_text_color));
                tv.setText(mItemTexts[i]);
            }

            // 添加view到容器中
            addView(view);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;

        /**
         * 根据传入的参数，分别获取测量模式和测量值
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 如果宽或者高的测量模式非精确值
         */
//        if (widthMode != MeasureSpec.EXACTLY
//                || heightMode != MeasureSpec.EXACTLY)
//        {
//            // 主要设置为背景图的高度
//            resWidth = getSuggestedMinimumWidth();
//            // 如果未设置背景图片，则设置为屏幕宽高的默认值
//            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;
//
//            resHeight = getSuggestedMinimumHeight();
//            // 如果未设置背景图片，则设置为屏幕宽高的默认值
//            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
//        } else
//        {
            // 如果都设置为精确值，则直接取小值；
            resWidth = resHeight = Math.min(width, height);
//        }

        setMeasuredDimension(resWidth, resHeight);

        // 获得半径
        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

        // menu item数量
        final int count = getChildCount();
        // menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        // menu item测量模式
        int childMode = MeasureSpec.EXACTLY;

        // 迭代测量
        for (int i = 0; i < count; i++)
        {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
            {
                continue;
            }

            // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
            int makeMeasureSpec;

            if (child.getId() ==R.id.circle_center_img)
            {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION),
                        childMode);
            } else
            {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                        childMode);
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }

    public interface OnMenuItemClickListener
    {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);
    }

    /**
     * MenuItem的点击事件接口
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 设置MenuItem的点击事件接口
     *
     * @param mOnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(
            OnMenuItemClickListener mOnMenuItemClickListener)
    {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
}
