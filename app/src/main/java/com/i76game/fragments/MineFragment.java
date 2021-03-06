package com.i76game.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.activity.AccountSecurityActivity;
import com.i76game.activity.BackActivity;
import com.i76game.activity.CustomerServiceActivity;
import com.i76game.activity.InformationActivity;
import com.i76game.activity.InviteActivity;
import com.i76game.activity.LoginActivity;
import com.i76game.activity.OrderActivity;
import com.i76game.activity.RechargeActivity;
import com.i76game.activity.SettingActivity;
import com.i76game.activity.UserGiftActivity;
import com.i76game.activity.UserInfoActivity;
import com.i76game.bean.MineRVBean;
import com.i76game.bean.MoneyBean;
import com.i76game.pay.OnPaymentListener;
import com.i76game.pay.PaymentCallbackInfo;
import com.i76game.pay.PaymentErrorMsg;
import com.i76game.utils.Global;
import com.i76game.utils.LogUtils;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.SharePrefUtil;
import com.i76game.utils.Utils;
import com.i76game.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 我的页面
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private Activity mActivity;
    private List<MineRVBean> mBeanList;
    private TextView mLogin;
    private boolean mIsLogin;
    private int loginCode = 10;
    private int settingCode = 11;
    private CircleImageView mine_icon;
    private TextView mRemain;
    private TextView code_tv;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MoneyBean moneyBean = (MoneyBean) msg.obj;
            LogUtils.i("mHandler");
            mRemain.setText(moneyBean.getRemain());
            code_tv.setText(moneyBean.getIntegral());
        }
    };
    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i("收到了登录广播");
            getUserMessage();
            Intent intent1 = new Intent("getInfo");
            getActivity().sendBroadcast(intent1);
            //让用户名那里设置为不可点击
            mIsLogin = false;
        }

    };

    private BroadcastReceiver exitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i("收到了登出广播");
            mIsLogin = true;
            mLogin.setText("立即登陆");
            mRemain.setText("0.00");
            code_tv.setText("0");
        }
    };

    private BroadcastReceiver moneyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i("收到了获取积分广播");
            getUserMessage();
        }
    };

    private BroadcastReceiver infoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i("收到了获取详情广播");
            mLogin.setText(Utils.isEmpty(MyApplication.userInfoBean.getNickname()) ? SharePrefUtil.getString(MyApplication.getContextObject(),
                    SharePrefUtil.KEY.NICHENG, "立即登陆") : MyApplication.userInfoBean.getNickname());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, null);
        mActivity = getActivity();
        initView(view);
        initData();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mine_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MineRVAdapter());
        return view;
    }

    private void initView(View view) {
        Button recharge = (Button) view.findViewById(R.id.mine_recharge);
        recharge.setOnClickListener(this);
        mRemain = (TextView) view.findViewById(R.id.mine_remain);
        code_tv = (TextView) view.findViewById(R.id.code_tv);
        mLogin = (TextView) view.findViewById(R.id.mine_login);
        mine_icon = (CircleImageView) view.findViewById(R.id.mine_icon);
        mine_icon.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mIsLogin = SharePrefUtil.getBoolean(MyApplication.getContextObject(),
                SharePrefUtil.KEY.FIRST_LOGIN, true);
        if (MyApplication.userInfoBean != null){
            mLogin.setText(Utils.isEmpty(MyApplication.userInfoBean.getNickname()) ? SharePrefUtil.getString(MyApplication.getContextObject(),
                    SharePrefUtil.KEY.NICHENG, "立即登陆") : MyApplication.userInfoBean.getNickname());
        }
        setItemOnClickListener(new ItemOnClickListener() {
            @Override
            public void onClickListener(int position) {
                if (position == 0) {
                    //返利申请
                    startActivity(new Intent(mActivity, BackActivity.class));
                } else if (position == 1) {
                    //申请记录
                } else {
                    if (position == 2) {
                        //邀请好友
                        startActivity(new Intent(getActivity(), InviteActivity.class));
//                        Utils.showShare(getActivity(), "欢迎下载176Game", "", "", "http://down.shouyoucun.cn/sdkgame/syc_60123/syc_60123.apk");
                    } else if (position == 3) {
                        //客服中心
                        startActivity(new Intent(mActivity, CustomerServiceActivity.class));
                    } else if (position == 4) {
                        //我的消息
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), InformationActivity.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("title", "我的消息");
                        startActivity(intent);
                    } else if (position == 5) {
                        //我的礼包
                        goActivity(new Intent(mActivity, UserGiftActivity.class));
                    } else if (position == 6) {
                        //消费明细
                        goActivity(new Intent(mActivity, OrderActivity.class));
                    } else if (position == 7) {
                        //帐号安全
                        goActivity(new Intent(mActivity, AccountSecurityActivity.class));
                    } else if (position == 8) {
                        //设置中心
                        Intent intent = new Intent(mActivity, SettingActivity.class);
                        startActivityForResult(intent, settingCode);
                    }
                }
            }
        });


    }

    private void goActivity(Intent intent) {
        if (!mIsLogin) {
            //要已经登陆了才可以 继续操作
            startActivity(intent);
        } else {
            Toast.makeText(mActivity, "请先登录哦", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        mBeanList = new ArrayList<>();
        int[] imageResource = {R.mipmap.ic_mine_reward, R.mipmap.ic_mine_record, R.mipmap.ic_mine_people
                , R.mipmap.ic_mine_kefu, R.mipmap.ic_mine_message, R.mipmap.ic_mine_gift1,
                R.mipmap.ic_mine_coin, R.mipmap.ic_mine_security, R.mipmap.ic_mine_settings,};
        String[] title = {"返利申请", "申请记录", "邀请好友", "客服中心", "我的消息",
                "我的礼包", "货币明细", "帐号安全", "设置管理"};
        for (int i = 0; i < imageResource.length; i++) {
            MineRVBean bean = new MineRVBean();
            bean.setImageResource(imageResource[i]);
            bean.setTitle(title[i]);
            if (i == 0) {
                bean.setContent("充值有奖，元宝返还");
            } else if (i == 2) {
                bean.setContent("最高奖100平台币/人");
            } else if (i == 3) {
                bean.setContent("问题反馈，帮助");
            } else {
                bean.setContent("");
            }
            mBeanList.add(bean);
        }
        if (!mIsLogin) {
            getUserMessage();
        }
    }

//

    /**
     * 获取用户信息
     */
    public void getUserMessage() {
        LogUtils.i("获取用户信息：" + Global.MONEY_URL);
        OkHttpUtil.getdata(Global.MONEY_URL, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                LogUtils.i("获取用户信息：" + res);
                parseJson(res);
            }
        });

    }

    /**
     * 解析平台币
     *
     * @param res 成功返回的json数据
     */
    private void parseJson(String res) {
        String remain = "0";
        try {
            JSONObject jsonObject = new JSONObject(res);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                String data = jsonObject.getString("data");
                jsonObject = new JSONObject(data);
                remain = jsonObject.getString("remain");
                jsonObject = new JSONObject(remain);
//                MoneyBean moneyBean = JsonUtil.parse(remain, MoneyBean.class);
                String integral = jsonObject.getString("integral");
                String ptb = jsonObject.getString("remain");
                MoneyBean moneyBean = new MoneyBean();
                moneyBean.setRemain(ptb);
                moneyBean.setIntegral(integral);
                Message message = Message.obtain();
                message.obj = moneyBean;
                mHandler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
            MoneyBean moneyBean = new MoneyBean();
            moneyBean.setRemain(remain);
            moneyBean.setIntegral("0");
            Message message = Message.obtain();
            message.obj = moneyBean;
            mHandler.sendMessage(message);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_login:
                if (mIsLogin) {
                    //没有登陆的话才可以点击登陆
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivityForResult(intent, loginCode);
                }
                break;
            case R.id.mine_icon:
                if (mIsLogin) {
                    //没有登陆的话才可以点击登陆
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivityForResult(intent, loginCode);
                } else {
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                }
                break;
            case R.id.mine_recharge:
                RechargeActivity.paymentListener = new OnPaymentListener() {
                    @Override
                    public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
                        Toast.makeText(mActivity, "充值金额数："
                                + callbackInfo.money + " 消息提示："
                                + callbackInfo.msg, Toast.LENGTH_LONG)
                                .show();
                        getUserMessage();
                    }

                    @Override
                    public void paymentError(PaymentErrorMsg errorMsg) {
                        Toast.makeText(
                                mActivity,
                                "充值失败：code:" + errorMsg.code + "  ErrorMsg:"
                                        + errorMsg.msg + "  预充值的金额："
                                        + errorMsg.money, Toast.LENGTH_LONG).show();
                    }
                };
                String url = "http://www.shouyoucun.cn/float.php/Mobile/Wallet/charge";
                Intent i = new Intent(mActivity, RechargeActivity.class);
                i.putExtra("url", url);
                i.putExtra("title", "支付充值");
                i.putExtra("hs-token", "支付充值");
                i.putExtra("timestamp", "支付充值");
                startActivity(i);
                break;
        }
    }

//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == loginCode && resultCode == Activity.RESULT_OK) {
//            String userName = data.getStringExtra("user_name");
//            mLogin.setText(userName);
//
//            getUserMessage();
//            //让用户名那里设置为不可点击
//            mIsLogin = false;
//        } else if (requestCode == settingCode && resultCode == Activity.RESULT_OK) {
//            //退出了登录
//            mIsLogin = true;
//            mLogin.setText("立即登陆");
//            mRemain.setText("0.00");
//        }
//    }

    class MineRVAdapter extends RecyclerView.Adapter<MineRVHolder> {

        @Override
        public MineRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.mine_rv_layout, parent, false);
            return new MineRVHolder(view);
        }

        @Override
        public void onBindViewHolder(MineRVHolder holder, final int position) {
            MineRVBean bean = mBeanList.get(position);
            holder.mContent.setText(bean.getContent());
            holder.mTitle.setText(bean.getTitle());
            holder.mImageResource.setBackgroundResource(bean.getImageResource());
            holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemOnClickListener != null) {
                        mItemOnClickListener.onClickListener(position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBeanList == null ? 0 : mBeanList.size();
        }
    }


    class MineRVHolder extends RecyclerView.ViewHolder {
        ImageView mImageResource;
        TextView mTitle;
        TextView mContent;
        LinearLayout mItemLayout;

        MineRVHolder(View itemView) {
            super(itemView);
            mImageResource = (ImageView) itemView.findViewById(R.id.mine_rv_image);
            mTitle = (TextView) itemView.findViewById(R.id.mine_rv_title);
            mContent = (TextView) itemView.findViewById(R.id.mine_rv_content);
            mItemLayout = (LinearLayout) itemView.findViewById(R.id.mine_rv_item);
        }
    }

    private ItemOnClickListener mItemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        mItemOnClickListener = itemOnClickListener;
    }

    interface ItemOnClickListener {
        void onClickListener(int position);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(loginReceiver, new IntentFilter("login"));
        getActivity().registerReceiver(exitReceiver, new IntentFilter("exit"));
        getActivity().registerReceiver(moneyReceiver, new IntentFilter("money"));
        getActivity().registerReceiver(infoReceiver, new IntentFilter("setInfo"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(loginReceiver);
        getActivity().unregisterReceiver(exitReceiver);
        getActivity().unregisterReceiver(moneyReceiver);
        getActivity().unregisterReceiver(infoReceiver);
    }
}
