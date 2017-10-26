package com.i76game.view;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/2.
 */

public class GiftDialog extends Dialog {
    private Context mContext;
   private ArrayList<String> mArrayList;

    public GiftDialog(Context context, ArrayList<String> arrayList) {
        super(context);
        mContext=context;
       mArrayList=arrayList;
    }

    public GiftDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    protected GiftDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.gift_dialog, null);
        setContentView(view);

        Window dialogWindow=getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams lp=dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        //设置宽高
        lp.width=(int)(d.widthPixels*0.8);
        dialogWindow.setAttributes(lp);

        TextView code = (TextView) findViewById(R.id.gift_dialog_code);
        TextView state = (TextView) findViewById(R.id.gift_dialog_state);
        TextView content= (TextView) findViewById(R.id.gift_dialog_content);

        content.setText("礼包内容："+mArrayList.get(2));//礼包内容
        code.setText(mArrayList.get(1));//礼包码
        state.setText(mArrayList.get(0));//领取状态

        Button copy = (Button) view.findViewById(R.id.gift_dialog_copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(
                        Context.CLIPBOARD_SERVICE);
                cmb.setText(mArrayList.get(1));
                Toast.makeText(mContext,"复制成功", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
