package com.i76game.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.i76game.R;
import com.i76game.activity.GameContentActivity;
import com.i76game.bean.KaifubiaoBean;
import com.i76game.utils.Global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/10/20.
 */

public class TableListAdapter extends RecyclerView.Adapter<TableListHolder>{

    private List<KaifubiaoBean> mBeanList;
    private Activity mActivity;
    private RequestManager mManager;
    public TableListAdapter(List<KaifubiaoBean> beanList, Activity mActivity){
        this.mBeanList=beanList;
        this.mActivity = mActivity;
        mManager = Glide.with(mActivity);
    }

    @Override
    public TableListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mActivity)
                .inflate(R.layout.server_recycle_layout,parent,false);
        return new TableListHolder(view);
    }

    public void addData(List<KaifubiaoBean> beanList) {
        mBeanList.addAll(beanList);
        notifyDataSetChanged();
    }

    public List<KaifubiaoBean> getDateList(){
        if (mBeanList == null){
            mBeanList = new ArrayList<>();
        }
        return mBeanList;
    }

    @Override
    public void onBindViewHolder(final TableListHolder holder, int position) {
        final KaifubiaoBean tableBean=mBeanList.get(position);
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String data = sdf.format(new Date(Long.valueOf(tableBean.D + "000")));
        holder.data.setText(tableBean.A + "  " + data);
        holder.gameName.setText(tableBean.B);
        holder.area.setText(tableBean.C);
        if (!tableBean.gamedata.icon.equals("")&&Integer.valueOf(tableBean.gamedata.id)!=0){

            mManager.load(tableBean.gamedata.icon).placeholder(R.mipmap.load_icon)
                    .error(R.mipmap.load_icon).into(holder.icon);
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        Intent intent3 = new Intent(mActivity, GameContentActivity.class);
//                        intent3.putExtra(Global.GAME_ID, serverBean.getGameId());
//                        startActivity(intent3);
                    startContentActivity(tableBean, holder);
                }
            });
        }else{
            holder.icon.setImageResource(R.mipmap.load_icon);
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity,"敬请期待",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 启动游戏详情界面
     */
    private void startContentActivity(KaifubiaoBean tableBean, TableListHolder holder) {
        Intent intent=new Intent(mActivity,GameContentActivity.class);
        intent.putExtra(Global.GAME_ID, Integer.valueOf(tableBean.gamedata.id));
        holder.icon.getDrawingCache(true);
        Global.drawable=holder.icon.getDrawable();
        //如果5.0以上使用
        if (android.os.Build.VERSION.SDK_INT > 20) {
            mActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    mActivity, holder.icon,
                    mActivity.getResources().getString(R.string.share_img)).toBundle());
        } else {
            mActivity.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mBeanList == null ? 0 : mBeanList.size();
    }
}

class TableListHolder extends RecyclerView.ViewHolder{
    TextView gameName;
    TextView data;
    TextView area;
    ImageView icon;
    Button state;
    public TableListHolder(View itemView) {
        super(itemView);
        area= (TextView) itemView.findViewById(R.id.table_recycle_area);
        data= (TextView) itemView.findViewById(R.id.table_recycle_data);
        gameName= (TextView) itemView.findViewById(R.id.table_recycle_game_name);
        icon= (ImageView) itemView.findViewById(R.id.table_recycle_icon);
        state= (Button) itemView.findViewById(R.id.table_recycle_state);
    }
}
