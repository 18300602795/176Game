package com.i76game.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.utils.AuthCodeUtil;
import com.i76game.utils.Global;
import com.i76game.utils.LogUtils;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.SharePrefUtil;
import com.i76game.utils.Utils;
import com.i76game.view.LoginDialog;

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

public class RegisterPhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEditUserName;
    private EditText mEditPassword;
    private EditText mEditProve;
    private EditText invite_et;
    private Button mBtnProve, mBtnRegister;
    private Toolbar register_toolbar;
    //用户输入的文字
    private String username, password, code;
    private boolean mIsVisibility = false;//是否能看到密码
    private LoginDialog mLoginDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int arg = msg.arg1;
            if (200 == arg) {
                Toast.makeText(RegisterPhoneActivity.this, "欢迎来到76游戏", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("user_name", (String) msg.obj);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (404 == arg) {
                Toast.makeText(RegisterPhoneActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private String mSessionId;
    private ImageView mVisibilityImage;

    @Override
    protected int setLayoutResID() {
        return R.layout.register_phone_layout;
    }

    @Override
    public void initView() {
        setToolbar("注册", R.id.register_toolbar);
        register_toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            register_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        mEditUserName = (EditText) findViewById(R.id.register_phone_edit_account);
        mEditPassword = (EditText) findViewById(R.id.register_phone_edit_password);
        mEditProve = (EditText) findViewById(R.id.register_phone_edit_prove);
        invite_et = (EditText) findViewById(R.id.invite_et);
        mBtnProve = (Button) findViewById(R.id.register_phone_btn_prove);
        mBtnProve.setOnClickListener(this);
        mBtnRegister = (Button) findViewById(R.id.register_phone_btn_register);
        mBtnRegister.setOnClickListener(this);
        mVisibilityImage = (ImageView) findViewById(R.id.register_phone_password_invisible);
        mVisibilityImage.setOnClickListener(this);
        mLoginDialog = new LoginDialog(this);
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
                        runOnUiThread(new Runnable() {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnProve.setText("重新发送");
                        mBtnProve.setClickable(true);
                        mBtnProve.setBackgroundResource(R.mipmap.btn_gradient);

                    }
                });
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
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isPhoneNumberValid(username)) {
            Toast.makeText(this, "手机号有误", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "密码只能由6至12位英文或数字组成", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code) && code.length() != 4) {
            Toast.makeText(this, "请输入4位的短信验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        //去访问借口，开始注册，
        mLoginDialog.show();
        mLoginDialog.setName("正在注册...");
        getCheckCode();
    }

    private void getCheckCode() {
//        if (mSessionId == null) {
//            return;
//        }
        String string_code = invite_et.getText().toString();

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("type", 1 + "");
        map.put("username", AuthCodeUtil.authcodeEncode(username, Global.appkey));
        map.put("mobile", AuthCodeUtil.authcodeEncode(username, Global.appkey));
        map.put("smscode", code + "");
        map.put("sessionid", mSessionId + "");
        map.put("password", AuthCodeUtil.authcodeEncode(password, Global.appkey) + "");
        if (!Utils.isEmpty(string_code))
            map.put("invite", string_code);
        map.put("deviceid", "");

//        map.put("client",)
        LogUtils.i("游戏注册接口：" + Utils.getCompUrlFromParams(Global.USER_ADD, map));
        OkHttpUtil.postFormEncodingdata(Global.USER_ADD, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginDialog.cancel();
                Message message = Message.obtain();
                message.arg1 = 404;
                message.obj = "注册失败, 请检查您的网络";
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mLoginDialog.cancel();
                String res = response.body().string().trim();
                LogUtils.i("res: " + res);
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
