package com.i76game.activity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.adapter.MainPagerAdapter;
import com.i76game.bean.UserInfoBean;
import com.i76game.fragments.BaseFragment;
import com.i76game.fragments.HomeFragment;
import com.i76game.fragments.InformationFragment;
import com.i76game.fragments.MineFragment;
import com.i76game.fragments.ServerFragment;
import com.i76game.inter.Imylistener;
import com.i76game.update.VersionUpdateManager;
import com.i76game.utils.Global;
import com.i76game.utils.JsonUtil;
import com.i76game.utils.LogUtils;
import com.i76game.utils.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView mHomeImage;
    private ImageView mServerImage;
    private ImageView mMessageImage;
    private ImageView mMineImage;
    private TextView mHomeText;
    private TextView mServerText;
    private TextView mMessageText;
    private TextView mMineText;
    private TextView mFanliText;
    private MainPagerAdapter pagerAdapter;
    private List<Fragment> fragments;

    private ViewPager main_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDate();
        //默认选中第一个
        mHomeImage.setImageResource(R.mipmap.ic_main_home_p);
        mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));

        //检查更新
        new VersionUpdateManager(this);
    }

    private void initDate() {
        fragments = new ArrayList<>();
        addFragment();
        pagerAdapter = new MainPagerAdapter(getFragmentManager(), fragments);
        main_pager.setAdapter(pagerAdapter);
    }

    private void addFragment() {
        HomeFragment homeFragment2 = new HomeFragment();
        homeFragment2.setListener(new Imylistener() {
            @Override
            public void Onclick() {
                reset();
                showTitle(3);
                main_pager.setCurrentItem(3);
            }
        });
        ServerFragment mServerFragment2 = new ServerFragment();
        InformationFragment mMessageFragment = new InformationFragment();
        MineFragment mMineFragment = new MineFragment();
        fragments.add(homeFragment2);
        fragments.add(mServerFragment2);
        fragments.add(mMessageFragment);
        fragments.add(mMineFragment);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        LinearLayout homeLayout = (LinearLayout) findViewById(R.id.main_home_layout);
        LinearLayout serverLayout = (LinearLayout) findViewById(R.id.main_home_server);
        LinearLayout fanliLayout = (LinearLayout) findViewById(R.id.main_home_fanli);
        LinearLayout messageLayout = (LinearLayout) findViewById(R.id.main_home_message);
        LinearLayout mineLayout = (LinearLayout) findViewById(R.id.main_home_mine);
        main_pager = (ViewPager) findViewById(R.id.main_pager);
        main_pager.setOffscreenPageLimit(3);
        homeLayout.setOnClickListener(this);
        serverLayout.setOnClickListener(this);
        fanliLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
        getUserInfo();
        mHomeImage = (ImageView) findViewById(R.id.main_home_image);
        mServerImage = (ImageView) findViewById(R.id.main_server_image);
        mMessageImage = (ImageView) findViewById(R.id.main_message_image);
        mMineImage = (ImageView) findViewById(R.id.main_mine_image);
        mFanliText = (TextView) findViewById(R.id.main_fanli_text);
        mHomeText = (TextView) findViewById(R.id.main_home_text);
        mServerText = (TextView) findViewById(R.id.main_server_text);
        mMessageText = (TextView) findViewById(R.id.main_message_text);
        mMineText = (TextView) findViewById(R.id.main_mine_text);

        main_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reset();
                showTitle(position);
                if (((BaseFragment) (pagerAdapter.getItem(position))).isShow) {
                    LogUtils.i(position + "：开始加载数据");
                    ((BaseFragment) (pagerAdapter.getItem(position))).initDate();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_home_layout:
                main_pager.setCurrentItem(0);
                reset();
                showTitle(0);
                break;
            case R.id.main_home_server:
                reset();
                showTitle(1);
                main_pager.setCurrentItem(1);
                break;
            case R.id.main_home_fanli:
//                reset();
//                replaceFragment(newFanliFragment());
//                currentFragment=newFanliFragment();
//                mSearchLayout.setVisibility(View.GONE);
//                mTitleLayout.setVisibility(View.VISIBLE);
//                mTitleText.setText("返利申请");
//                mFanliText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                Intent intent = new Intent(this, CircleMenuActivity.class);
                startActivity(intent);

                break;
            case R.id.main_home_message:
                reset();
                showTitle(2);
                main_pager.setCurrentItem(2);
                break;

            case R.id.main_home_mine:
                reset();
                showTitle(3);
                main_pager.setCurrentItem(3);
                break;

        }
    }


    /**
     * 重置各个item
     */
    private void reset() {
        mMineImage.setImageResource(R.mipmap.ic_main_mine);
        mMessageImage.setImageResource(R.mipmap.ic_main_message);
        mServerImage.setImageResource(R.mipmap.ic_main_server);
        mHomeImage.setImageResource(R.mipmap.ic_main_home);
        mMineText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mServerText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mMessageText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mFanliText.setTextColor(getResources().getColor(R.color.main_item_text_color));
    }

    private void showTitle(int i) {
        switch (i) {
            case 0:
                mHomeImage.setImageResource(R.mipmap.ic_main_home_p);
                mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
            case 1:
                mServerImage.setImageResource(R.mipmap.ic_main_server_p);
                mServerText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
            case 2:
                main_pager.setCurrentItem(2);
                mMessageImage.setImageResource(R.mipmap.ic_main_message_p);
                mMessageText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
            case 3:
                mMineImage.setImageResource(R.mipmap.ic_main_mine_p);
                mMineText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
        }
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        LogUtils.i("获取用户详情：" + Global.INFO_URL);
        ArrayMap<String, String> map = new ArrayMap<>();
        OkHttpUtil.postFormEncodingdata(Global.INFO_URL, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                LogUtils.e("获取用户详情：" + res);
                parseJson(res);
            }
        });
    }

    /**
     * 解析平台币
     *
     * @param res 成功返回的json数据
     */
    private void parseJson(String res) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                String data = jsonObject.getString("data");
                MyApplication.userInfoBean = JsonUtil.parse(data, UserInfoBean.class);
                Intent intent = new Intent("setInfo");
                sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }
    }


    /**
     * 点击2次退出程序
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    private BroadcastReceiver infoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i("收到了用户信息");
            getUserInfo();
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(infoReceiver, new IntentFilter("getInfo"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(infoReceiver);
    }
}
