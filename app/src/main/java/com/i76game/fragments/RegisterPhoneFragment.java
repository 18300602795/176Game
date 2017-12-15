package com.i76game.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.utils.AuthCodeUtil;
import com.i76game.utils.Global;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 手机号注册
 */

public class RegisterPhoneFragment extends Fragment implements View.OnClickListener {
    private EditText mEditUserName;
    private EditText mEditPassword;
    private EditText mEditProve;
    private Button mBtnProve, mBtnRegister;
    //用户输入的文字
    private String username, password, code;
    private boolean mIsVisibility = false;//是否能看到密码
    private Activity mActivity;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int arg = msg.arg1;
            if (200 == arg) {
                Toast.makeText(mActivity, "欢迎来到76游戏", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("user_name", (String) msg.obj);
                mActivity.setResult(Activity.RESULT_OK, intent);
                mActivity.finish();
            } else if (404 == arg) {
                Toast.makeText(mActivity, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private String mSessionId;
    private ImageView mVisibilityImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_phone_layout, null);
        mEditUserName = (EditText) view.findViewById(R.id.register_phone_edit_account);
        mEditPassword = (EditText) view.findViewById(R.id.register_phone_edit_password);
        mEditProve = (EditText) view.findViewById(R.id.register_phone_edit_prove);
        mBtnProve = (Button) view.findViewById(R.id.register_phone_btn_prove);
        mBtnProve.setOnClickListener(this);
        mBtnRegister = (Button) view.findViewById(R.id.register_phone_btn_register);
        mBtnRegister.setOnClickListener(this);
        mVisibilityImage = (ImageView) view.findViewById(R.id.register_phone_password_invisible);
        mVisibilityImage.setOnClickListener(this);
        mActivity = getActivity();
        return view;
    }

    public void initText() {
        username = mEditUserName.getText().toString().trim();
        password = mEditPassword.getText().toString().trim();
        code = mEditProve.getText().toString().trim();
    }


    //定于变量
    private boolean tag = true;
    private int i = 60; //定于获取验证码60秒

    @Override
    public void onClick(View v) {
        initText();
        switch (v.getId()) {
            case R.id.register_phone_btn_prove:
                if (!isvalidate()) {
                    break;
                }
                changeBtnGetCode();
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("type", 1 + "");
                String str = AuthCodeUtil.authcodeEncode(username, Global.appkey);
                map.put("mobile", str);
                getValidateCode(Global.SMS_CODE_SEND, map);
                break;
            case R.id.register_phone_btn_register:
                register();
                break;
            case R.id.register_phone_password_invisible:
                if (!mIsVisibility) {
                    mVisibilityImage.setBackgroundResource(R.mipmap.ic_password_visible);
                    mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIsVisibility = true;
                } else {
                    mVisibilityImage.setBackgroundResource(R.mipmap.ic_password_invisible);
                    mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIsVisibility = false;
                }
                break;
        }
    }

    /**
     * 验证码改变
     */
    private void changeBtnGetCode() {
        new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        if (mActivity == null) {
                            break;
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBtnProve.setText("重新发送(" + i + ")");
                                mBtnProve.setClickable(false);
                                mBtnProve.setBackgroundResource(R.drawable.prove_btn_bg);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBtnProve.setText("重新发送");
                            mBtnProve.setClickable(true);
                            mBtnProve.setBackgroundResource(R.mipmap.btn_gradient);

                        }
                    });
                }
            }
        }.start();
    }

    public void getValidateCode(String Url, ArrayMap<String, String> map) {
        // 定义要访问的接口和要强转的实体
        OkHttpUtil.postFormEncodingdata(Url, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                try {
                    parseProveJson(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //解析验证码
    private void parseProveJson(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        int code = jsonObject.getInt("code");
        Message message = Message.obtain();
        if (code >= 200 && code <= 250) {
            String data = jsonObject.getString("data");
            jsonObject = new JSONObject(data);
            mSessionId = jsonObject.getString("sessionid");
        } else {
            message.arg1 = 404;
            message.obj = jsonObject.getString("msg");
            mHandler.sendMessage(message);
        }
    }

    private boolean isvalidate() {
        // 获取控件输入的值
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(mActivity, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isPhoneNumberValid(username)) {
            Toast.makeText(mActivity, "手机号有误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 判断手机号有没有错
     *
     * @param number 手机号
     */
    public boolean isPhoneNumberValid(String number) {
        boolean isValid = false;
        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = number;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void register() {
        // 1.首先判断输入的值是否有效
        // 2.然后判断输入的验证码是否有效（防止没有点击获取验证码自己填的错误验证码)
        // 3.最后注册
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,12})");
        if (!isvalidate()) {
            return;
        }
        if (!p.matcher(password).matches()) {
            Toast.makeText(mActivity, "密码只能由6至12位英文或数字组成", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code) && code.length() != 4) {
            Toast.makeText(mActivity, "请输入4位的短信验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        //去访问借口，开始注册，
        getCheckCode();
    }

    private void getCheckCode() {
        if (mSessionId == null) {
            return;
        }
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("type", 1 + "");
        map.put("username", AuthCodeUtil.authcodeEncode(username, Global.appkey));
        map.put("mobile", AuthCodeUtil.authcodeEncode(username, Global.appkey));
        map.put("smscode", code + "");
        map.put("sessionid", mSessionId + "");
        map.put("password", AuthCodeUtil.authcodeEncode(password, Global.appkey) + "");
        map.put("deviceid", "");

//        map.put("client",)
        OkHttpUtil.postFormEncodingdata(Global.USER_ADD, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
//                Log.e("------", "res: "+ res);
                try {
                    parseLoginJson(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void parseLoginJson(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        int code = jsonObject.getInt("code");
        Message message = Message.obtain();
        if (code >= 200 && code <= 250) {
            String data = jsonObject.getString("data");
            jsonObject = new JSONObject(data);
            String identifier = jsonObject.getString("identifier");
            String accesstoken = jsonObject.getString("accesstoken");
            int expaireTime = jsonObject.getInt("expaire_time");
//            Log.e("------", "identifier: "+ identifier);
//            Log.e("------", "accesstoken: "+ accesstoken);
//            Log.e("------", "expaire_time: "+ expaireTime);
            SharePrefUtil.saveString(MyApplication.getContextObject(), SharePrefUtil.KEY.IDENTIFIER, identifier);
            SharePrefUtil.saveString(MyApplication.getContextObject(), SharePrefUtil.KEY.ACCESSTOKEN, accesstoken);//token
            SharePrefUtil.saveInt(MyApplication.getContextObject(), SharePrefUtil.KEY.EXPAIRE_TIME, expaireTime);//时间
            SharePrefUtil.saveBoolean(MyApplication.getContextObject(), SharePrefUtil.KEY.FIRST_LOGIN, false);  //表示用户已经登录了，以后都要保持这个状态
            SharePrefUtil.saveString(MyApplication.getContextObject(), SharePrefUtil.KEY.NICHENG, username);//保存用户的账号，（也就是昵称）
            message.arg1 = 200;
            message.obj = username;
            mHandler.sendMessage(message);
        } else {
            message.arg1 = 404;
            message.obj = jsonObject.getString("msg");
            mHandler.sendMessage(message);
        }
    }


    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
