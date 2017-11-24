package com.i76game.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.i76game.R;

/**
 * Created by Administrator on 2017/11/16.
 */

public class LoginDialog extends Dialog {
    private Context context;
    private TextView name_tv;

    //实现构造方法
    public LoginDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //重写onCreate方法，
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏，注意此处的去掉标题栏，并不是去掉当前activity的标题栏，而是去掉自定义的dialog的标题
        //如果不去掉现实的dialog会有一个空白的头部。由此可见我们的dialog和activity是如此的相像。
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    /**
     * 初始化自定义的布局界面，使用inflater将自定义的布局压进我们的当前dialog窗体。
     */
    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        //自定义布局转换成view
        View view = inflater.inflate(R.layout.dialog_login, null);
        //设置到当前的窗体
        setContentView(view);
        //拿到自定义布局的各个控件
        name_tv = (TextView) view.findViewById(R.id.name_tv);
        Window dialogWindow = getWindow();
        //设置圆角样式
        dialogWindow.setBackgroundDrawableResource(R.drawable.dialog_style);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        // 高度设置为屏幕的0.6
        //lp.height =(int) (d.heightPixels * 0.6);
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    public void setName(String name) {
        name_tv.setText(name);
    }
}
