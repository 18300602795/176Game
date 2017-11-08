package com.i76game.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.i76game.R;
import com.i76game.activity.GameInfoActivity;
import com.i76game.bean.ServerBean;
import com.i76game.utils.Global;
import com.i76game.view.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 开服表
 */

public class ServerFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private List<ServerBean> mTodayList=new ArrayList<>();
    private List<ServerBean> mYesterdayList=new ArrayList<>();
    private List<ServerBean> mTomorrowList=new ArrayList<>();

    private String mWay;
    private RequestManager mManager;

    private Button mTodayBtn;
    private Button mTomorrowBtn;
    private Button mYesterdayBtn;

    private final int TODAY_DAT=0;
    private final int TOMORROW_DAT=1;
    private final int YESTERDAY_DAT=2;
    private final int FAILURE=3;

    private Activity mActivity;
    private LoadDialog mLoadDialog;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TODAY_DAT:
                    mRecyclerView.setAdapter(new TableListAdapter(mTodayList));
                    hideDialog();
                    break;
                case TOMORROW_DAT:
                    mRecyclerView.setAdapter(new TableListAdapter(mTomorrowList));
                    break;

                case YESTERDAY_DAT:
                    mRecyclerView.setAdapter(new TableListAdapter(mYesterdayList));
                    break;

                case FAILURE:
                    hideDialog();
                    Toast.makeText(mActivity,"网络似乎有点问题",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.server_fragment,null);
        initView(view);
        mActivity=getActivity();
        mManager = Glide.with(ServerFragment.this);
        //获取当前星期几
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

        //第一次进来先选中
        mTodayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);

        if (mLoadDialog==null){
            mLoadDialog = new LoadDialog(mActivity,true,"100倍加速中");
        }
        mLoadDialog.show();
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(Global.SERVER_URL).build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                mHandler.sendEmptyMessage(FAILURE);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String res = response.body().string().trim();
                analysisDate(res);
            }
        });

        return view;
    }

    private void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.table_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));

        mTodayBtn = (Button) view.findViewById(R.id.table_today);
        mTomorrowBtn = (Button) view.findViewById(R.id.table_tomorrow);
        mYesterdayBtn = (Button) view.findViewById(R.id.table_yesterday);
        mTodayBtn.setOnClickListener(this);
        mTomorrowBtn.setOnClickListener(this);
        mYesterdayBtn.setOnClickListener(this);
    }


    /**
     * 解析json数据
     * @param str 服务器返回的json字符串
     */
    private void analysisDate(String str) {
        boolean isYesterday=true;
        try {
            //服务器数据这样，只能这样解析
            for (int i=1;i<500;i++){
                JSONObject jsonObject = new JSONObject(str)
                        .getJSONObject("game_"+i);

                if (i>1){
                    ServerBean bean=new ServerBean();
                    //获取时间戳并将其转换为正常时间
                    String d=jsonObject.getString("D");
                    String format = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                    String data=sdf.format(new Date(Long.valueOf(d+"000")));

                    data=data.split(" ")[1];
                    bean.setData(data);
                    bean.setGameName(jsonObject.getString("B"));//B代表游戏名
                    bean.setGameArea(jsonObject.getString("C"));//A代表要开服的区
                    bean.setWeek(jsonObject.getString("A"));//A代表星期


                    String gameDataStr=jsonObject.getString("gamedata");
                    if (!gameDataStr.equals("No search to the data")){
                        JSONObject gameDataObj=new JSONObject(gameDataStr);
                        bean.setIconUrl(gameDataObj.getString("icon"));
                        bean.setGameId(gameDataObj.getInt("id"));

                    }else{
                        bean.setIconUrl("");
                        bean.setGameId(0);
                    }

                    if (isOneDay(bean.getWeek())){
                        mTodayList.add(bean);
                        isYesterday=false;
                    }else {
                        if (isYesterday){
                            mYesterdayList.add(bean);
                        }else{
                            mTomorrowList.add(bean);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mHandler.sendEmptyMessage(TODAY_DAT);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table_today:
                resetView();
                mHandler.sendEmptyMessage(TODAY_DAT);
                mTodayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
            case R.id.table_tomorrow:
                resetView();
                mHandler.sendEmptyMessage(TOMORROW_DAT);
                mTomorrowBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
            case R.id.table_yesterday:
                resetView();
                mHandler.sendEmptyMessage(YESTERDAY_DAT);
                mYesterdayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
        }
    }

    /**
     * 重置所有button
     */
    private void resetView(){
        mTodayBtn.setBackgroundResource(R.drawable.server_btn_bg);
        mTomorrowBtn.setBackgroundResource(R.drawable.server_btn_bg);
        mYesterdayBtn.setBackgroundResource(R.drawable.server_btn_bg);
    }


    class TableListAdapter extends RecyclerView.Adapter<TableListHolder>{

        private List<ServerBean> mBeanList;
        public TableListAdapter(List<ServerBean> beanList){
            mBeanList=beanList;
        }

        @Override
        public TableListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(mActivity)
                    .inflate(R.layout.server_recycle_layout,parent,false);
            return new TableListHolder(view);
        }

        @Override
        public void onBindViewHolder(final TableListHolder holder, int position) {
            final ServerBean serverBean=mBeanList.get(position);
            holder.data.setText(serverBean.getWeek()+"  "+serverBean.getData());
            holder.gameName.setText(serverBean.getGameName());
            holder.area.setText(serverBean.getGameArea());
            if (!serverBean.getIconUrl().equals("")&&serverBean.getGameId()!=0){
                mManager.load(serverBean.getIconUrl()).placeholder(R.mipmap.load_icon)
                        .error(R.mipmap.load_icon).into(holder.icon);
                holder.state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent3 = new Intent(mActivity, GameContentActivity.class);
//                        intent3.putExtra(Global.GAME_ID, serverBean.getGameId());
//                        startActivity(intent3);
                        startContentActivity(serverBean, holder);
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
        private void startContentActivity(ServerBean tableBean, TableListHolder holder) {
            Intent intent=new Intent(getActivity(),GameInfoActivity.class);
            intent.putExtra(Global.GAME_ID, tableBean.getGameId());
            holder.icon.getDrawingCache(true);
            Global.drawable=holder.icon.getDrawable();
            //如果5.0以上使用
            if (android.os.Build.VERSION.SDK_INT > 20) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(), holder.icon,
                        getResources().getString(R.string.share_img)).toBundle());
            } else {
                startActivity(intent);
            }
        }

        @Override
        public int getItemCount() {
            return mBeanList.size();
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


    private boolean isOneDay(String week){
        String way="星期日";
        if (mWay.equals("1")){
            way="星期日";
        }else if (mWay.equals("2")){
            way="星期一";
        }else if (mWay.equals("3")){
            way="星期二";
        }else if (mWay.equals("4")){
            way="星期三";
        }else if (mWay.equals("5")){
            way="星期四";
        }else if (mWay.equals("6")){
            way="星期五";
        }else if (mWay.equals("7")){
            way="星期六";
        }

        if (week.equals(way)){
            return true;
        }
        return false;
    }

    /**
     * 隐藏对话框
     */
    private void hideDialog(){
        if (mLoadDialog!=null){
            mLoadDialog.dismiss();
        }
    }
}
