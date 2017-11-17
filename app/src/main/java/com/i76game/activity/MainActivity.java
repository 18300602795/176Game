package com.i76game.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.adapter.MainPagerAdapter;
import com.i76game.fragments.HomeFragment2;
import com.i76game.fragments.InformationFragment;
import com.i76game.fragments.MineFragment;
import com.i76game.fragments.ServerFragment2;
import com.i76game.inter.Imylistener;
import com.i76game.update.VersionUpdateManager;
import com.i76game.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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
    private TextView mTitleText;
    private RelativeLayout mTitleLayout;//标题
    private RelativeLayout mSearchLayout;//搜索
    private LinearLayout mToolLayout;//包裹搜索的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDate();
        //默认选中第一个
        mHomeImage.setImageResource(R.mipmap.ic_main_home_p);
        mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
        main_pager = (ViewPager) findViewById(R.id.main_pager);

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
        HomeFragment2 homeFragment2 = new HomeFragment2();
        homeFragment2.setListener(new Imylistener() {
            @Override
            public void Onclick() {
                reset(3);
                main_pager.setCurrentItem(3);
            }
        });
        ServerFragment2 mServerFragment2 = new ServerFragment2();
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
        homeLayout.setOnClickListener(this);
        serverLayout.setOnClickListener(this);
        fanliLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);

        ImageView mineImage = (ImageView) findViewById(R.id.main_img_mine);
        mineImage.setOnClickListener(this);
        mHomeImage = (ImageView) findViewById(R.id.main_home_image);
        mServerImage = (ImageView) findViewById(R.id.main_server_image);
        mMessageImage = (ImageView) findViewById(R.id.main_message_image);
        mMineImage = (ImageView) findViewById(R.id.main_mine_image);
        mFanliText = (TextView) findViewById(R.id.main_fanli_text);
        mHomeText = (TextView) findViewById(R.id.main_home_text);
        mServerText = (TextView) findViewById(R.id.main_server_text);
        mMessageText = (TextView) findViewById(R.id.main_message_text);
        mMineText = (TextView) findViewById(R.id.main_mine_text);

        mTitleLayout = (RelativeLayout) findViewById(R.id.main_title_layout);
        mToolLayout = (LinearLayout) findViewById(R.id.main_tool_layout);
        mTitleText = (TextView) findViewById(R.id.main_title_text);
        mSearchLayout = (RelativeLayout) findViewById(R.id.main_search_layout);
        mSearchLayout.setOnClickListener(this);
        ImageView downloadBtn = (ImageView) findViewById(R.id.main_download);
        downloadBtn.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mToolLayout.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        main_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reset(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_home_layout:
                main_pager.setCurrentItem(0);
                reset(0);
                break;
            case R.id.main_home_server:
                reset(1);
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
                reset(2);
                main_pager.setCurrentItem(2);
                break;

            case R.id.main_img_mine:
            case R.id.main_home_mine:
                reset(3);
                main_pager.setCurrentItem(3);
                break;

            case R.id.main_download:
                startActivity(new Intent(this, DownloadActivity.class));
                break;

            case R.id.main_search_layout:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
    }


    /**
     * 重置各个item
     */
    private void reset(int i) {
        mMineImage.setImageResource(R.mipmap.ic_main_mine);
        mMessageImage.setImageResource(R.mipmap.ic_main_message);
        mServerImage.setImageResource(R.mipmap.ic_main_server);
        mHomeImage.setImageResource(R.mipmap.ic_main_home);
        mMineText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mServerText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mMessageText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mFanliText.setTextColor(getResources().getColor(R.color.main_item_text_color));
        switch (i){
            case 0:
                mHomeImage.setImageResource(R.mipmap.ic_main_home_p);
                mTitleLayout.setVisibility(View.GONE);
                mToolLayout.setVisibility(View.GONE);
                mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
            case 1:
                mServerImage.setImageResource(R.mipmap.ic_main_server_p);
                mToolLayout.setVisibility(View.GONE);
                mTitleLayout.setVisibility(View.VISIBLE);
                mTitleText.setText("开服表");
                mServerText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
            case 2:
                main_pager.setCurrentItem(2);
                mToolLayout.setVisibility(View.GONE);
                mTitleLayout.setVisibility(View.VISIBLE);
                mTitleText.setText("我的消息");
                mMessageImage.setImageResource(R.mipmap.ic_main_message_p);
                mMessageText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
            case 3:
                mToolLayout.setVisibility(View.GONE);
                mTitleLayout.setVisibility(View.GONE);
                mMineImage.setImageResource(R.mipmap.ic_main_mine_p);
                mMineText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                break;
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

}
