package com.i76game.activity;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.utils.Utils;
import com.i76game.view.LuckPanLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/21.
 */

public class LuckActivity extends BaseActivity implements LuckPanLayout.AnimationEndListener, View.OnClickListener {
    private LuckPanLayout luckPanLayout;
    private String[] strs;
    private RelativeLayout title_rl;
    private ImageView back_return;
    private TextView hit_user_tv;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_lucky;
    }

    @Override
    public void initView() {
        strs = getResources().getStringArray(R.array.names);
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.setAnimationEndListener(this);
        hit_user_tv = (TextView) findViewById(R.id.hit_user_tv);
        back_return = (ImageView) findViewById(R.id.back_return);
        title_rl = (RelativeLayout) findViewById(R.id.title_rl);
        back_return.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            title_rl.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
    }

    public void rotation(View view) {
        int item = -1;
        Random random = new Random();
        int i = random.nextInt(10000);
        if (i == 0) {
            item = 0;
        } else if (i <= 10) {
            item = 1;
        } else if (i <= 20) {
            item = 2;
        } else if (i <= 100) {
            item = 3;
        } else if (i <= 500) {
            item = 4;
        } else if (i <= 1000) {
            item = 5;
        } else if (i <= 5000) {
            item = 6;
        } else {
            item = 7;
        }
        Log.i("333", "i：" + i);
        Log.i("333", "item：" + item);
        luckPanLayout.rotate(item, 100);
    }

    @Override
    public void endAnimation(int position) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        Toast.makeText(this, "Position = " + position + "," + strs[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_return:
                finish();
                break;
        }
    }
}
