package com.i76game.activity;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.utils.Utils;

/**
 * Created by Administrator on 2017/11/16.
 */

public class EditWorldActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar edit_toolbar;
    private EditText old_et, new_et, affirm_et;
    private TextView edit_btn;

    @Override
    protected int setLayoutResID() {
        return R.layout.editworld_activity;
    }

    @Override
    public void initView() {
        setToolbar("修改密码", R.id.edit_toolbar);
        edit_toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            edit_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        old_et = (EditText) findViewById(R.id.old_et);
        new_et = (EditText) findViewById(R.id.new_et);
        edit_btn = (TextView) findViewById(R.id.edit_btn);
        affirm_et = (EditText) findViewById(R.id.affirm_et);

        edit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_btn:
                break;
        }
    }
}
