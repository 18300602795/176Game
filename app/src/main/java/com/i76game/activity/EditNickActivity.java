package com.i76game.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.utils.Utils;

/**
 * Created by Administrator on 2017/11/16.
 */

public class EditNickActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar edit_toolbar;
    private EditText nick_et;
    private TextView edit_btn;
    private String nick;

    @Override
    protected int setLayoutResID() {
        return R.layout.editnick_activity;
    }

    @Override
    public void initView() {
        setToolbar("修改昵称", R.id.edit_toolbar);
        edit_toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            edit_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        nick_et = (EditText) findViewById(R.id.nick_et);
        edit_btn = (TextView) findViewById(R.id.edit_btn);

        edit_btn.setOnClickListener(this);
        nick = getIntent().getStringExtra("nick");
        nick_et.setHint(nick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_btn:
                String nick = nick_et.getText().toString();
                if (nick == null || nick.equals("")){
                    Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("nick", nick);
                    setResult(102, intent);
                    finish();
                }
                break;
        }
    }
}
