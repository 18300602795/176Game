package com.i76game.fragments;

import android.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/11/23.
 */

public class BaseFragment extends Fragment {
    public boolean isShow = true;

    public void initDate() {
    }

    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
