package com.i76game.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 登陆界面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditUserName;
    private EditText mEditPassword;
    private String username = null;
    private String password = null;
    private ImageView mVisibilityImage;
    private boolean mIsVisibility=false;

    private int mSucceed=200;
    private int mFailure=400;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int arg=msg.arg1;
            if (mSucceed==arg){
                showToast("欢迎回来", Toast.LENGTH_SHORT);
                Intent intent=new Intent();
                intent.putExtra("user_name",username);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }else if (mFailure==arg){
                showToast("帐号或密码错误", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        mEditUserName = (EditText) findViewById(R.id.login_edit_account);
        mEditPassword = (EditText) findViewById(R.id.login_edit_password);
        Button btnLogin= (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
        mVisibilityImage = (ImageView) findViewById(R.id.login_password_invisible);
        mVisibilityImage.setOnClickListener(this);


        TextView forgetPassword= (TextView) findViewById(R.id.login_forget_password);
        TextView userRegister= (TextView) findViewById(R.id.login_user_register);
        forgetPassword.setOnClickListener(this);
        userRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                initText();
                actionLogin();
                break;
            case R.id.login_password_invisible:
                if (!mIsVisibility){
                    mVisibilityImage.setBackgroundResource(R.mipmap.ic_password_visible);
                    mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIsVisibility=true;
                }else{
                    mVisibilityImage.setBackgroundResource(R.mipmap.ic_password_invisible);
                    mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIsVisibility=false;
                }
                break;


            case R.id.login_forget_password:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                helpIntent.putExtra(HelpActivity.TYPE_TO_HELP,HelpActivity.HELP_8_FORGET_PASSWORD);
                startActivity(helpIntent);
                break;

            case R.id.login_user_register:
                Intent intent = new Intent(this, UserRegisterActivity.class);
                startActivityForResult(intent,userRegisterCode);
                break;
        }
    }

    private int userRegisterCode=20;
    public void initText() {
        username = mEditUserName.getText().toString().trim();
        password = mEditPassword.getText().toString().trim();
    }

    private void actionLogin() {
        if (TextUtils.isEmpty(username)) {
            showToast( "请输入账号",0);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码",0);
            return;
        }
        Pattern pat = Pattern.compile("([a-zA-Z0-9]{6,12})");
        if (!pat.matcher(username).matches()) {
            showToast("账号只能由6至12位英文或数字组成",0);
            return;
        }
        if (!pat.matcher(password).matches()) {
            showToast("密码只能由6至12位英文或数字组成",0);
            return;
        }

        /**
         * 执行登陆传参数
         */
        loginRemoteService(username, password);
    }

    private void loginRemoteService(final String username, String password) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("username", AuthCodeUtil.authcodeEncode(username,Global.appkey));
        map.put("password", AuthCodeUtil.authcodeEncode(password, Global.appkey));

        OkHttpUtil.postFormEncodingdata(Global.LOGIN_URL, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();

                try {
                    parseJson(res, username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 将返回的数据解析并保存
     */
    private void parseJson(String res, String username) throws JSONException {
        JSONObject jsonObject=new JSONObject(res);
        int code = jsonObject.getInt("code");
        Message message=new Message();
        if (code>=200&&code<=250){
            String data=jsonObject.getString("data");
            jsonObject = new JSONObject(data);
            String identifier=jsonObject.getString("identifier");
            String accesstoken=jsonObject.getString("accesstoken");
            int expaireTime=jsonObject.getInt("expaire_time");
            SharePrefUtil.saveString(MyApplication.getContextObject(), SharePrefUtil.KEY.IDENTIFIER, identifier);
            SharePrefUtil.saveString(MyApplication.getContextObject(), SharePrefUtil.KEY.ACCESSTOKEN, accesstoken);//token
            SharePrefUtil.saveInt(MyApplication.getContextObject(), SharePrefUtil.KEY.EXPAIRE_TIME, expaireTime);//时间
            SharePrefUtil.saveBoolean(MyApplication.getContextObject(), SharePrefUtil.KEY.FIRST_LOGIN, false);  //表示用户已经登录了，以后都要保持这个状态
            SharePrefUtil.saveString(MyApplication.getContextObject(), SharePrefUtil.KEY.NICHENG, username);//保存用户的账号，（也就是昵称）
            message.arg1=mSucceed;
            mHandler.sendMessage(message);
        }else{
            message.arg1=mFailure;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==userRegisterCode&&resultCode==Activity.RESULT_OK){
            String userRegisterName = data.getStringExtra("user_name");
            Log.e("==loginResult===",""+userRegisterName);
            Intent intent=new Intent();
            intent.putExtra("user_name",userRegisterName);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
