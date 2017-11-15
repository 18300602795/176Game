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
import com.i76game.utils.Code;
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
 * 用户名注册fragment
 */
public class RegisterUserFragment extends Fragment implements View.OnClickListener {

    private EditText mEditUserName;
    private EditText mEditPassword;

    private EditText mEditProve;
    private Activity mActivity;
    //用户输入的文字
    private String username,password,code;
    private String mRealCode;
    private ImageView mImageProve;
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
                Toast.makeText(mActivity,"欢迎来到76游戏",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("user_name",(String)msg.obj);
                mActivity.setResult(Activity.RESULT_OK,intent);
                mActivity.finish();
            }else if (mFailure==arg){
                Toast.makeText(mActivity,(String)msg.obj,Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.register_user_layout,null);
        mEditUserName = (EditText) view.findViewById(R.id.register_user_edit_account);
        mEditPassword = (EditText) view.findViewById(R.id.register_user_edit_password);
        mEditProve = (EditText) view.findViewById(R.id.register_user_edit_prove);
        mImageProve = (ImageView) view.findViewById(R.id.register_user_image_prove);
        mImageProve.setOnClickListener(this);
        mImageProve.setImageBitmap(Code.getInstance().createBitmap());
        mRealCode = Code.getInstance().getCode().toLowerCase();
        Button btnRegister= (Button) view.findViewById(R.id.register_user_btn_register);
        btnRegister.setOnClickListener(this);
        mVisibilityImage = (ImageView) view.findViewById(R.id.register_user_password_invisible);
        mVisibilityImage.setOnClickListener(this);

        mActivity=getActivity();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_user_image_prove:
                mImageProve.setImageBitmap(Code.getInstance().createBitmap());
                mRealCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.register_user_btn_register:
                username = mEditUserName.getText().toString().trim();
                password = mEditPassword.getText().toString().trim();
                code=mEditProve.getText().toString().trim();
                if (isvalidate()){
                    loginRemoteService(username,password);
                }
                break;


            case R.id.register_user_password_invisible:
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
        }
    }


    private boolean isvalidate() {

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(mActivity,"请输入账号",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mActivity,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!code.equals(mRealCode)) {
            Toast.makeText(mActivity,"请输入正确的验证码",Toast.LENGTH_SHORT).show();
            return false;
        }

        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,12})");
        if (!p.matcher(username).matches()) {
            Toast.makeText(mActivity,"账号只能由6至12位英文或数字组成",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!p.matcher(password).matches()) {
            Toast.makeText(mActivity,"密码只能由6至12位英文或数字组成",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loginRemoteService(final String username, String password) {

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("type", 2 + "");
        map.put("username", AuthCodeUtil.authcodeEncode(username, Global.appkey));
        map.put("password", AuthCodeUtil.authcodeEncode(password, Global.appkey));
        map.put("deviceid", "");

        OkHttpUtil.postFormEncodingdata(Global.USER_ADD, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                try {
                    parseJson(res,username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void parseJson(String res, String username) throws JSONException {
        JSONObject jsonObject=new JSONObject(res);
        int code = jsonObject.getInt("code");
        Message message=Message.obtain();
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
            message.obj=username;
            mHandler.sendMessage(message);
        }else{
            message.arg1=mFailure;
            message.obj=jsonObject.getString("msg");
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
