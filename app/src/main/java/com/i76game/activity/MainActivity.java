package com.i76game.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.i76game.fragments.FanliFragment;
import com.i76game.fragments.HomeFragment;
import com.i76game.fragments.MessageFragment;
import com.i76game.fragments.MineFragment;
import com.i76game.fragments.ServerFragment2;
import com.i76game.update.VersionUpdateManager;
import com.i76game.utils.Utils;

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
    private FragmentManager mFragmentManager;

    private TextView mTitleText;
    private RelativeLayout mTitleLayout;//标题
    private RelativeLayout mSearchLayout;//搜索
    private LinearLayout mToolLayout;//包裹搜索的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //默认选中第一个
        mHomeImage.setImageResource(R.mipmap.ic_main_home_p);
        mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));

        replaceFragment(newHomeFragment());
        currentFragment = newHomeFragment();

        //检查更新
        new VersionUpdateManager(this);
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
    }
    /**
     * 设置状态栏透明
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
                if (currentFragment != mHomeFragment) {
                    reset();
                    replaceFragment(newHomeFragment());
                    currentFragment = newHomeFragment();
                    mHomeImage.setImageResource(R.mipmap.ic_main_home_p);
                    mTitleLayout.setVisibility(View.GONE);
                    mToolLayout.setVisibility(View.VISIBLE);
                    mHomeText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                }

                break;
            case R.id.main_home_server:
                if (currentFragment != mServerFragment) {
                    reset();
                    replaceFragment(newServerFragment());
                    currentFragment = newServerFragment();
                    mServerImage.setImageResource(R.mipmap.ic_main_server_p);
                    mToolLayout.setVisibility(View.GONE);
                    mTitleLayout.setVisibility(View.VISIBLE);
                    mTitleText.setText("开服表");
                    mServerText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                }
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
                if (currentFragment != mMessageFragment) {
                    reset();
                    replaceFragment(newMessageFragment());
                    currentFragment = newMessageFragment();
                    mToolLayout.setVisibility(View.GONE);
                    mTitleLayout.setVisibility(View.VISIBLE);
                    mTitleText.setText("我的消息");
                    mMessageImage.setImageResource(R.mipmap.ic_main_message_p);
                    mMessageText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                }
                break;

            case R.id.main_img_mine:
            case R.id.main_home_mine:
                if (currentFragment != mMineFragment) {
                    reset();
                    replaceFragment(newMineFragment());
                    currentFragment = newMineFragment();
                    mToolLayout.setVisibility(View.GONE);
                    mTitleLayout.setVisibility(View.GONE);
                    mMineImage.setImageResource(R.mipmap.ic_main_mine_p);
                    mMineText.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                }
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

    private Fragment currentFragment;

    public void replaceFragment(Fragment fragment) {
        if (mFragmentManager == null) {
            mFragmentManager = getFragmentManager();
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.main_frame_layout, fragment);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        currentFragment = fragment;
        transaction.commit();
    }

    private Fragment mHomeFragment;
    private Fragment mMineFragment;
    private Fragment mServerFragment;
    private Fragment mMessageFragment;
    private Fragment mFanliFragment;

    private Fragment newHomeFragment() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            return mHomeFragment;
        } else {
            return mHomeFragment;
        }
    }

    private Fragment newMineFragment() {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
            return mMineFragment;
        } else {
            return mMineFragment;
        }
    }

    private Fragment newServerFragment() {
        if (mServerFragment == null) {
            mServerFragment = new ServerFragment2();
            return mServerFragment;
        } else {
            return mServerFragment;
        }
    }

    private Fragment newMessageFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
            return mMessageFragment;
        } else {
            return mMessageFragment;
        }
    }

    private Fragment newFanliFragment() {
        if (mFanliFragment == null) {
            mFanliFragment = new FanliFragment();
            return mFanliFragment;
        } else {
            return mFanliFragment;
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
