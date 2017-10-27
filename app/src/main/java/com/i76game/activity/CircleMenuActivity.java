package com.i76game.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.i76game.R;
import com.i76game.view.Circle;

/**
 * Created by Administrator on 2017/5/16.
 */

public class CircleMenuActivity extends Activity {
    private int[] images = {R.mipmap.ic_mine_fanli, R.mipmap.ic_mine_gift, R.mipmap.ic_mine_huodong,
            R.mipmap.ic_mine_gonglue, R.mipmap.ic_mine_kefu1, R.mipmap.ic_mine_luntan};
    private String[] texts = {"返利", "礼包", "活动", "攻略", "客服", "论坛"};
    private Circle mCircleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_menu);
        mCircleMenu = (Circle) findViewById(R.id.main_circle_menu);
        mCircleMenu.setMenuItemIconsAndTexts(images, texts);
        mCircleMenu.setOnMenuItemClickListener(new Circle.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                if (pos == 0) {
                    startActivity(new Intent(CircleMenuActivity.this, GiftListActivity.class));
                } else if (pos == 1) {
                    startActivity(new Intent(CircleMenuActivity.this, InformationActivity.class));
                } else if (pos == 2) {
                    startActivity(new Intent(CircleMenuActivity.this, BackActivity.class));
                }
            }

            @Override
            public void itemCenterClick(View view) {
                finishAnimator();
            }
        });


        //圆形菜单出来的动画
        ObjectAnimator animationScaleY = ObjectAnimator.ofFloat(mCircleMenu, "scaleY", 0.3f, 1);
        ObjectAnimator animationScaleX = ObjectAnimator.ofFloat(mCircleMenu, "scaleX", 0.3f, 1);
        ObjectAnimator animationRotation = ObjectAnimator.ofFloat(mCircleMenu, "rotation", 0, 360);
        ObjectAnimator animationAlpha = ObjectAnimator.ofFloat(mCircleMenu, "alpha", 0.3f, 1);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animationScaleY, animationScaleX,
                animationRotation, animationAlpha);
        set.start();

        RelativeLayout outLayout = (RelativeLayout) findViewById(R.id.main_circle_out);
        outLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAnimator();
            }
        });
    }


    private int mIsBack = 0;

    @Override
    public void onBackPressed() {
        if (mIsBack == 0) {
            finishAnimator();
            mIsBack = 1;
        }
    }

    private void finishAnimator() {
        ObjectAnimator animationScaleY = ObjectAnimator.ofFloat(mCircleMenu, "scaleY", 1, 0.3f);
        ObjectAnimator animationScaleX = ObjectAnimator.ofFloat(mCircleMenu, "scaleX", 1, 0.3f);
        ObjectAnimator animationRotation = ObjectAnimator.ofFloat(mCircleMenu, "rotation", 1, -360f);
        ObjectAnimator animationAlpha = ObjectAnimator.ofFloat(mCircleMenu, "alpha", 1, 0.3f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animationScaleY, animationScaleX,
                animationRotation, animationAlpha);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
    }
}
