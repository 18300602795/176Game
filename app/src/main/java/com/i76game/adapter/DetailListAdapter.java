package com.i76game.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.i76game.R;
import com.i76game.bean.GameContentBean;
import com.i76game.fragments.GameDetailFragment;
import com.i76game.fragments.GiftFragment;
import com.i76game.fragments.MessageFragment2;
import com.i76game.fragments.StrategyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 */

public class DetailListAdapter extends BaseAdapter {
    private Activity context;
    private GameContentBean.DataBean mData;
    private FragmentManager fm;
    private GameDetailFragment gameDrtailFragment;
    public StrategyFragment strategyFragment;
    public MessageFragment2 messageFragment2;
    public GiftFragment giftFragment;
    private List<Fragment> fragments;
    private List<GameContentBean.GameListBean> gameListBeen;

    public DetailListAdapter(Activity context, GameContentBean.DataBean mData, List<GameContentBean.GameListBean> gameListBeen) {
        this.context = context;
        this.mData = mData;
        this.gameListBeen = gameListBeen;
        fm = context.getFragmentManager();
        fragments = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return mData;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        showFragment(1);
        return view;
    }

    public void showFragment(int type) {
        FragmentTransaction transaction = fm.beginTransaction();
        hideAll(transaction);
        switch (type) {
            case 1:
                if (gameDrtailFragment == null) {
                    gameDrtailFragment = new GameDetailFragment();
                    gameDrtailFragment.mData = mData;
                    gameDrtailFragment.gameListBeen = gameListBeen;
                    transaction.add(R.id.content_f, gameDrtailFragment);
                    fragments.add(gameDrtailFragment);
                }
                transaction.show(gameDrtailFragment);
                break;
            case 2:
                if (messageFragment2 == null) {
                    messageFragment2 = new MessageFragment2();
                    messageFragment2.isDate = true;
                    messageFragment2.post_type = "3";
                    messageFragment2.app_id = String.valueOf(mData.getGameid());
                    transaction.add(R.id.content_f, messageFragment2);
                    fragments.add(messageFragment2);
                }
                transaction.show(messageFragment2);
                break;
            case 3:

                if (strategyFragment == null) {
                    strategyFragment = new StrategyFragment();
                    strategyFragment.app_id = String.valueOf(mData.getGameid());
                    strategyFragment.post_type = "2";
                    transaction.add(R.id.content_f, strategyFragment);
                    fragments.add(strategyFragment);
                }
                transaction.show(strategyFragment);
                break;
            case 4:
                if (giftFragment == null) {
                    giftFragment = new GiftFragment();
                    giftFragment.app_id = String.valueOf(mData.getGameid());
                    transaction.add(R.id.content_f, giftFragment);
                    fragments.add(giftFragment);
                }
                transaction.show(giftFragment);
                break;
        }
        transaction.commit();
    }

    private void hideAll(FragmentTransaction transcation) {
        for (Fragment fragment : fragments) {
            transcation.hide(fragment);
        }
    }

}
