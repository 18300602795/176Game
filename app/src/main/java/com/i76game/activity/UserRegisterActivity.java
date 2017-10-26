package com.i76game.activity;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.i76game.R;
import com.i76game.fragments.RegisterAdapter;
import com.i76game.fragments.RegisterPhoneFragment;
import com.i76game.fragments.RegisterUserFragment;

import java.util.ArrayList;


/**
 * 用户注册
 */

public class UserRegisterActivity extends BaseActivity{
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles={"手机号注册","用户名注册"};
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_user_register;
    }

    @Override
    public void initView() {
        TabLayout tabLayout= (TabLayout) findViewById(R.id.register_tab_layout);
        ViewPager viewPager= (ViewPager) findViewById(R.id.register_view_pager);
        mFragments.add(new RegisterPhoneFragment());
        mFragments.add(new RegisterUserFragment());
        viewPager.setAdapter(new RegisterAdapter(getFragmentManager(),mFragments,mTitles));
        tabLayout.setupWithViewPager(viewPager);

        ImageView back= (ImageView) findViewById(R.id.register_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
