package com.i76game.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.bean.SignInfoBean;
import com.i76game.utils.Global;
import com.i76game.utils.JsonUtil;
import com.i76game.utils.LogUtils;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.Utils;
import com.i76game.view.LoginDialog;
import com.i76game.view.TipDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/15.
 */

public class SignActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar sign_toolbar;
    private TextView date_tv, num_tv, name_tv;
    private TextView sign_btn;
    private ImageView sign_iv1, sign_iv2, sign_iv3, sign_iv4, sign_iv5, sign_iv6, sign_iv7;
    private ImageView icon_iv;
    private LinearLayout demo_ll, invite_ll, photo_ll, info_ll, share_ll;
    private TipDialog tipDialog;
    private SignInfoBean signInfoBean;
    private List<ImageView> sign_ivs;
    private LoginDialog mLoginDialog;
    private View.OnClickListener signListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            try {
                SignInfoBean signInfoBean;
                switch (msg.what) {
                    case 1:
                        signInfoBean = (SignInfoBean) msg.obj;
                        long sign_time = signInfoBean.getSign_in_time();
                        Date sign_date = new Date(sign_time * 1000);
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String sign = simpleDateFormat.format(sign_date);
                        String today = simpleDateFormat.format(date);
                        LogUtils.i("today：" + today);
                        LogUtils.i("sign：" + sign);
                        if (sign.equals(today)) {
                            sign_btn.setBackgroundResource(R.drawable.sign_gray_bg);
                            sign_btn.setText("已签到");
                            sign_btn.setOnClickListener(signListener);
                        }
                        if (!sign.equals(today) && signInfoBean.getSign_in_days() == 7) {

                        } else {
                            for (int i = 0; i < signInfoBean.getSign_in_days(); i++) {
                                sign_ivs.get(i).setImageResource(R.mipmap.ic_check_box_checked);
                            }
                        }
                        num_tv.setText(Utils.setStyle("连续签到" + signInfoBean.getSign_in_days() + "天", "连续签到", "天"));
                        break;
                    case 2:
                        signInfoBean = (SignInfoBean) msg.obj;
                        num_tv.setText(Utils.setStyle("连续签到" + signInfoBean.getSign_in_days() + "天", "连续签到", "天"));
                        for (int i =signInfoBean.getSign_in_days(); i < 7; i ++){
                            sign_ivs.get(i).setImageResource(R.mipmap.ic_check_box_normal);
                        }
                        sign_ivs.get(signInfoBean.getSign_in_days() - 1).setImageResource(R.mipmap.ic_check_box_checked);
                        sign_btn.setBackgroundResource(R.drawable.sign_gray_bg);
                        sign_btn.setText("已签到");
                        Intent intent_receiver = new Intent("money");
                        sendBroadcast(intent_receiver);
                        Toast.makeText(SignActivity.this, "签到成功，获得" + signInfoBean.getIntegral() + "积分", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
        }
    };


    @Override
    protected int setLayoutResID() {
        return R.layout.sign_activity;
    }

    @Override
    public void initView() {
        setToolbar("每日签到", R.id.sign_toolbar);
        sign_toolbar = (Toolbar) findViewById(R.id.sign_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sign_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        mLoginDialog = new LoginDialog(this);
        tipDialog = new TipDialog(this);
        date_tv = (TextView) findViewById(R.id.date_tv);
        num_tv = (TextView) findViewById(R.id.num_tv);
        name_tv = (TextView) findViewById(R.id.name_tv);
        sign_btn = (TextView) findViewById(R.id.sign_btn);

        sign_iv1 = (ImageView) findViewById(R.id.sign_iv1);
        sign_iv2 = (ImageView) findViewById(R.id.sign_iv2);
        sign_iv3 = (ImageView) findViewById(R.id.sign_iv3);
        sign_iv4 = (ImageView) findViewById(R.id.sign_iv4);
        sign_iv5 = (ImageView) findViewById(R.id.sign_iv5);
        sign_iv6 = (ImageView) findViewById(R.id.sign_iv6);
        sign_iv7 = (ImageView) findViewById(R.id.sign_iv7);
        icon_iv = (ImageView) findViewById(R.id.icon_iv);

        demo_ll = (LinearLayout) findViewById(R.id.demo_ll);
        invite_ll = (LinearLayout) findViewById(R.id.invite_ll);
        photo_ll = (LinearLayout) findViewById(R.id.photo_ll);
        info_ll = (LinearLayout) findViewById(R.id.info_ll);
        share_ll = (LinearLayout) findViewById(R.id.share_ll);

        demo_ll.setOnClickListener(this);
        invite_ll.setOnClickListener(this);
        photo_ll.setOnClickListener(this);
        info_ll.setOnClickListener(this);
        share_ll.setOnClickListener(this);
        sign_btn.setOnClickListener(this);
        name_tv.setText(Utils.setStyle(name_tv.getText().toString(), "试玩", ""));
        sign_ivs = new ArrayList<>();
        sign_ivs.add(sign_iv1);
        sign_ivs.add(sign_iv2);
        sign_ivs.add(sign_iv3);
        sign_ivs.add(sign_iv4);
        sign_ivs.add(sign_iv5);
        sign_ivs.add(sign_iv6);
        sign_ivs.add(sign_iv7);
        mLoginDialog.show();
        mLoginDialog.setName("正在加载...");
        getSignMessage();
    }


    @Override
    public void initData() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        date_tv.setText(format.format(date));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.demo_ll:
                tipDialog.show();
                tipDialog.setTip("该功能正在开发中，敬请期待");
                tipDialog.setOnCallbackLister(new TipDialog.ClickListenerInterface() {
                    @Override
                    public void click(int id) {
                        switch (id) {
                            case R.id.cancel_btn:
                                tipDialog.cancel();
                                break;
                            case R.id.confirm_btn:
                                tipDialog.cancel();
                                break;
                        }
                    }
                });
                break;
            case R.id.invite_ll:
                startActivity(new Intent(this, InviteActivity.class));
                break;
            case R.id.photo_ll:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.info_ll:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.share_ll:
                startActivity(new Intent(this, InviteActivity.class));
                break;
            case R.id.sign_btn:
                upSign();
                break;
        }
    }

    /**
     * 签到
     */
    public void upSign() {
        LogUtils.i("签到：" + Global.UP_SIGN_MESSAGE);
        mLoginDialog.show();
        mLoginDialog.setName("正在签到...");
        ArrayMap<String, String> map = new ArrayMap<>();
        OkHttpUtil.postFormEncodingdata(Global.UP_SIGN_MESSAGE, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginDialog.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mLoginDialog.cancel();
                String res = response.body().string().trim();
                LogUtils.i("签到结果：" + res);
                parseJson2(res);
            }
        });
    }

    /**
     * 获取签到信息
     */
    public void getSignMessage() {
        LogUtils.i("获取签到信息：" + Global.SIGN_MESSAGE);
        ArrayMap<String, String> map = new ArrayMap<>();
        OkHttpUtil.postFormEncodingdata(Global.SIGN_MESSAGE, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginDialog.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mLoginDialog.cancel();
                String res = response.body().string().trim();
                LogUtils.i("获取签到信息：" + res);
                parseJson(res);
            }
        });
    }

    /**
     * 解析签到结果
     *
     * @param res 成功返回的json数据
     */
    private void parseJson2(String res) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                String data = jsonObject.getString("data");
                signInfoBean = JsonUtil.parse(data, SignInfoBean.class);
                Message message = Message.obtain();
                message.obj = signInfoBean;
                message.what = 2;
                handler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }
    }

    /**
     * 解析签到信息
     *
     * @param res 成功返回的json数据
     */
    private void parseJson(String res) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                String data = jsonObject.getString("data");
                signInfoBean = JsonUtil.parse(data, SignInfoBean.class);
                Message message = Message.obtain();
                message.obj = signInfoBean;
                message.what = 1;
                handler.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }
    }
}
