package com.i76game.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.i76game.R;
import com.i76game.utils.Utils;

import java.util.Calendar;

/**
 * 返利界面
 */
public class BackActivity extends BaseActivity implements View.OnClickListener{

    private EditText mSelectDateBtn;
    private ImageView back_return;
    private RelativeLayout title_rl;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_back;
    }

    @Override
    public void initView() {
        Button postBtn= (Button) findViewById(R.id.back_post);
        mSelectDateBtn = (EditText) findViewById(R.id.back_select_date);
        back_return = (ImageView) findViewById(R.id.back_return);
        title_rl = (RelativeLayout) findViewById(R.id.title_rl);
        back_return.setOnClickListener(this);
        mSelectDateBtn.setFocusable(false);//使其不能编辑
        postBtn.setOnClickListener(this);
        mSelectDateBtn.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            title_rl.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_select_date:
                showDateDialog();
                break;
            case R.id.back_post:
                postData();
                break;
            case R.id.back_return:
                finish();
                break;
        }

    }

    /**
     * 提交数据
     */
    private void postData() {
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("time","");
        map.put("money","");
        map.put("person_name","");
        map.put("game_area","");
        map.put("game_name","");
        map.put("game_id","");
//        OkHttpUtil.postFormEncodingdata(url,false,map,new );


    }

    /**
     * 显示时间选择器
     */
    private void showDateDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(BackActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mSelectDateBtn.setText(""+year+"-"+(month+1)+"-"+dayOfMonth);
            }
        }, year, month, day).show();
    }
}
