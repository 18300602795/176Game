package com.i76game.activity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.bean.Prize;
import com.i76game.utils.Utils;
import com.i76game.view.LotteryView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 返利界面
 */
public class BackActivity extends BaseActivity implements View.OnClickListener{

    private EditText back_money;
    private EditText back_game_name;
    private EditText back_person_id;
    private EditText back_game_id;
    private EditText back_person_name;
    private EditText back_area;
    private EditText mSelectDateBtn;
    private ImageView back_return;
    private RelativeLayout title_rl;
    private GridView draw_gv;
    private LotteryView lottery;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_back;
    }

    @Override
    public void initView() {
        lottery=(LotteryView) findViewById(R.id.lottery);
        Button postBtn= (Button) findViewById(R.id.back_post);
        back_money = (EditText) findViewById(R.id.back_money);
        back_game_name = (EditText) findViewById(R.id.back_game_name);
        back_person_id = (EditText) findViewById(R.id.back_person_id);
        back_game_id = (EditText) findViewById(R.id.back_game_id);
        back_person_name = (EditText) findViewById(R.id.back_person_name);
        back_area = (EditText) findViewById(R.id.back_area);
        mSelectDateBtn = (EditText) findViewById(R.id.back_select_date);
        back_return = (ImageView) findViewById(R.id.back_return);
        title_rl = (RelativeLayout) findViewById(R.id.title_rl);
//        draw_gv = (GridView) findViewById(R.id.draw_gv);
        back_return.setOnClickListener(this);
        mSelectDateBtn.setFocusable(false);//使其不能编辑
        postBtn.setOnClickListener(this);
        mSelectDateBtn.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            title_rl.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }

        int[]prizesIcon={R.mipmap.action,R.mipmap.adventure,R.mipmap.iphone,R.mipmap.combat,R.mipmap.moba,R.mipmap.other,R.mipmap.role,R.mipmap.sports,R.mipmap.meizu};
        final List<Prize> prizes=new ArrayList<Prize>();
        for(int x=0;x<9;x++){
            Prize lottery=new Prize();
            lottery.setId(x+1);
            lottery.setName("Lottery"+(x+1));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), prizesIcon[x]);
            lottery.setIcon(bitmap);
            if((x+1)%2==0){
                lottery.setBgColor(0xff4fccee);
            }else if(x==4){
                lottery.setBgColor(0xffffffff);
            }else{
                lottery.setBgColor(0xff00ff34);
            }

            prizes.add(lottery);
        }
        lottery.setPrizes(prizes);
        lottery.setOnTransferWinningListener(new LotteryView.OnTransferWinningListener() {

            @Override
            public void onWinning(int position) {
                Toast.makeText(getApplicationContext(), prizes.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
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
        map.put("time",mSelectDateBtn.getText().toString());
        map.put("money",back_money.getText().toString());
        map.put("person_name",back_person_name.getText().toString());
        map.put("person_id",back_person_id.getText().toString());
        map.put("game_area",back_area.getText().toString());
        map.put("game_name",back_game_name.getText().toString());
        map.put("game_id",back_game_id.getText().toString());
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
