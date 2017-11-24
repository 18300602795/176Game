package com.i76game.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.i76game.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by cxm on 2016/9/24.
 */

public class CircleLoadingView extends View {

    private int centerX; // 控件x轴的中心
    private int centerY; // 控件y轴的中心

    private int radius;  // 小球的半径
    private int[] colors;  // 颜色数组
    private int maxOffset ; // 小球偏移的最大值
    private int minOffset ; // 小球偏移的最小值
    private int duration; // 动画执行的时间

    private AnimatorSet mAnimationSet; // 保存动画的集合，用于同时启动动画
    private Paint mPaint;  // 画笔
    private int mCanvasAngle; // 画笔目前的角度
    private float mOffset; // 小球目前的偏移


    public CircleLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);  // 初始化
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);  // 初始化
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);   // 初始化
    }

    private void init(AttributeSet attrs) {
        TypedArray array = this.getContext().obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        // 获取各个属性值
        if(array!=null){
            radius = array.getDimensionPixelSize(R.styleable.CircleLoadingView_radius,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getContext().getResources().getDisplayMetrics()));
            maxOffset = array.getDimensionPixelSize(R.styleable.CircleLoadingView_maxOffset,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,getContext().getResources().getDisplayMetrics()));
            minOffset = array.getDimensionPixelSize(R.styleable.CircleLoadingView_minOffset,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,12,getContext().getResources().getDisplayMetrics()));
            colors = array.getResources().getIntArray(array.getResourceId(R.styleable.CircleLoadingView_colors,R.array.colors));
            duration = array.getDimensionPixelSize(R.styleable.CircleLoadingView_duration,2000);
            array.recycle();
        }

        // 初始化画笔，去锯齿
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        // 更新视图：一个一个球依次更新视图
        for(int i = 0; i<colors.length;i++){
            // 更改画笔的颜色
            mPaint.setColor(colors[i]);
            // 根据不同的球来旋转画布
            canvas.rotate(mCanvasAngle+i*(360/colors.length),centerX,centerY);
            // 根据目前的偏移来决定小球的位置
            canvas.drawCircle(centerX+mOffset,centerY+mOffset,radius,mPaint);
            // 还原画布的旋转
            canvas.rotate(-(mCanvasAngle+i*(360/colors.length)),centerX,centerY);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取中心的坐标
        centerX = w/2;
        centerY = h/2;
        // 开启动画
        startAnim();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // 非EXACTLY模式下自定义控件的大小
        if(modeWidth == MeasureSpec.AT_MOST || modeWidth == MeasureSpec.UNSPECIFIED ){
            sizeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getContext().getResources().getDisplayMetrics());
            sizeWidth += getPaddingLeft()+getPaddingRight();
        }
        if(modeHeight == MeasureSpec.AT_MOST || modeHeight == MeasureSpec.UNSPECIFIED ){
            sizeHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getContext().getResources().getDisplayMetrics());
            sizeHeight += getPaddingBottom()+getPaddingTop();
        }

        setMeasuredDimension(sizeWidth,sizeHeight);
    }

    private void startAnim() {

        // 生成动画集合的实例
        mAnimationSet = new AnimatorSet();

        // 用于保存动画
        Collection<Animator> animList = new ArrayList<>();

        // 使用属性动画生成不断变化的偏移值

        // 参数这样设计是为了让当前小球的颜色为后一个小球的颜色
        ValueAnimator canvasAnim = new ValueAnimator().ofInt(0,  (colors.length+1)*180/colors.length, (colors.length+1)*360/colors.length);
        canvasAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasAngle = (int) animation.getAnimatedValue();
            }
        });
        // 设重复次数为1000是为了达到一个“无限”循环播放动画的效果
        canvasAnim.setRepeatCount(1000);
        canvasAnim.setRepeatMode(ValueAnimator.RESTART);

        // 加入集合
        animList.add(canvasAnim);

        // 使用属性动画生成不断的小球的偏移值

        ValueAnimator circleAnim = new ValueAnimator().ofFloat(maxOffset, minOffset, maxOffset);
        circleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        // 如上
        circleAnim.setRepeatCount(1000);
        circleAnim.setRepeatMode(ValueAnimator.RESTART);

        animList.add(circleAnim);



        // 设置动画集合的属性
        mAnimationSet.setDuration(duration);
        mAnimationSet.playTogether(animList);
        mAnimationSet.setInterpolator(new LinearInterpolator());
        // 开始动画
        mAnimationSet.start();


    }

    // 初始化动画，为了配合在Java设置各个属性
    private void initialAnim(){
        if(mAnimationSet!=null){
            if(mAnimationSet.isRunning()){
                mAnimationSet.end();
            }
            mAnimationSet = null;
            startAnim();
        }
    }

    // 设置颜色，颜色数组的长度决定了小球的个数
    public void setColors(int [] colors){
        this.colors = colors;
        initialAnim();

    }

    // 设置小球的半径
    public void setRadius(int radius){
        this.radius = radius;
    }

    // 设置小球的偏移
    public void setMaxMinSizeLength(int maxOffset,int minOffset){
        this.maxOffset = maxOffset;
        this.minOffset = minOffset;
        initialAnim();

    }

    // 设置动画执行的时间
    public void setDuration(int duration){
        this.duration = duration;
        initialAnim();

    }



}