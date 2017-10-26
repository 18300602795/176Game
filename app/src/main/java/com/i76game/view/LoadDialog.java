package com.i76game.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;

/**
 * Created by Administrator on 2017/5/24.
 */

public class LoadDialog extends Dialog {

    /**
     * LoadDialog
     */
    private LoadDialog loadDialog;
    /**
     * cancelable, the dialog dimiss or undimiss flag
     */
    private boolean cancelable;
    /**
     * if the dialog don't dimiss, what is the tips.
     */
    private String tipMsg;

    /**
     * the LoadDialog constructor
     *
     * @param ctx        Context
     * @param cancelable boolean
     * @param tipMsg     String
     */
    private Context mContext;
    public LoadDialog(final Context ctx, boolean cancelable, String tipMsg) {
        super(ctx);
        this.cancelable = cancelable;
        this.tipMsg = tipMsg;
        this.mContext=ctx;
        this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.dialog_layout);
        // 必须放在加载布局后
        setparams();
        TextView tv = (TextView) findViewById(R.id.tvLoad);
        if (!TextUtils.isEmpty(tipMsg)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(tipMsg);
        }
    }

    private void setparams() {
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(false);

        Window dialogWindow=getWindow();
        WindowManager.LayoutParams lp=dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        //设置宽高
        lp.width=(int)(d.widthPixels*0.7);
        dialogWindow.setAttributes(lp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!cancelable) {
                Toast.makeText(getContext(), tipMsg, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * show the dialog
     *
     * @param context
     */
    public void show(Context context) {
        show(context, null, true);
    }

    /**
     * show the dialog
     *
     * @param context Context
     * @param message String
     */
    public void show(Context context, String message) {
        show(context, message, true);
    }

    /**
     * show the dialog
     *
     * @param context    Context
     * @param resourceId resourceId
     */
    public void show(Context context, int resourceId) {
        show(context, context.getResources().getString(resourceId), true);
    }

    /**
     * show the dialog
     *
     * @param context    Context
     * @param message    String, show the message to user when isCancel is true.
     * @param cancelable boolean, true is can't dimiss，false is can dimiss
     */
    private void show(Context context, String message, boolean cancelable) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (loadDialog != null && loadDialog.isShowing()) {
            return;
        }
        loadDialog = new LoadDialog(context, cancelable, message);
        loadDialog.show();
    }

}
