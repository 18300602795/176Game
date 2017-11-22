package com.i76game.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.bean.Prize;
import com.i76game.view.LotteryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class PrizeActivity extends BaseActivity{
    LotteryView nl;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_prize;
    }

    @Override
    public void initView() {
        nl=(LotteryView) findViewById(R.id.nl);

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
        nl.setPrizes(prizes);
        nl.setOnTransferWinningListener(new LotteryView.OnTransferWinningListener() {

            @Override
            public void onWinning(int position) {
                Toast.makeText(getApplicationContext(), prizes.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
