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

public class TipDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView tip_tv;
    //声明自定义的接口
    private ClickListenerInterface clickListenerInterface;

    //实现构造方法
    public TipDialog(Context context) {
        super(context);
        this.context = context;
    }

    public TipDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TipDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    //自定义接口
    public interface ClickListenerInterface {
        void click(int id);
    }

    //接口的回调方法，以供实现接口调用
    public void setOnCallbackLister(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
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
        View view = inflater.inflate(R.layout.dialog_tip, null);
        //设置到当前的窗体
        setContentView(view);
        //拿到自定义布局的各个控件
        TextView confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        tip_tv = (TextView) findViewById(R.id.tip_tv);
        Window dialogWindow = getWindow();
        //设置圆角样式
        dialogWindow.setBackgroundDrawableResource(R.drawable.dialog_style);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        // 高度设置为屏幕的0.6
        //lp.height =(int) (d.heightPixels * 0.6);
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    public void setTip(String tip) {
        tip_tv.setText(tip);
    }

    @Override
    public void onClick(View view) {
        //控件点击事件使用我们的定义的接口中的方法，将点击的控件id传递进去。以待我们在activity中回调接口，取得点击的是哪一个控件。
        //方便传值
        clickListenerInterface.click(view.getId());
    }
}
