package com.i76game.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.adapter.TableListAdapter;
import com.i76game.bean.KaifubiaoBean;
import com.i76game.utils.Global;
import com.i76game.utils.JsonUtil;
import com.i76game.utils.LogUtils;
import com.i76game.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class YesterdayFragment extends BaseFragment {
    private View view;
    private XRecyclerView listView;
    private TableListAdapter mAdapter;
    private String mWay;
    public int yesterday_pager;
    private String result;
    private View layoutNoData;
    private LinearLayout loading_ll;
    private Handler hanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    hideDialog();
                    layoutNoData.setVisibility(View.GONE);
                    List<KaifubiaoBean> beans = (List<KaifubiaoBean>) msg.obj;
                    if (mAdapter.getDateList().size() == 0) {
                        mAdapter.addData((List<KaifubiaoBean>) msg.obj);
                    } else {
                        mAdapter = new TableListAdapter(beans, getActivity());
                        listView.setAdapter(mAdapter);
                    }
                    break;
                case 2:
                    hideDialog();
                    layoutNoData.setVisibility(View.GONE);
                    if (mAdapter.getDateList().size() == 0) {
                        layoutNoData.setVisibility(View.VISIBLE);
                    } else {
                    }

                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kaifubiao, null);
        listView = (XRecyclerView) view.findViewById(R.id.table_rv);
        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        layoutNoData = view.findViewById(R.id.layout_noData);
        loading_ll = (LinearLayout) view.findViewById(R.id.loading_ll);

        return view;
    }

    @Override
    public void initDate() {
        LogUtils.i("yesterday---开始加载数据");
        isShow = false;
        mAdapter = new TableListAdapter(null, getActivity());
        listView.setAdapter(mAdapter);
        getDate();
        listView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getDate();
            }

            @Override
            public void onLoadMore() {
                hideDialog();
                showToast("没有更多数据哦", Toast.LENGTH_SHORT);
            }
        });
    }

    public void getDate() {
        //获取当前星期几
        layoutNoData.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Log.e("333", "开始获取数据");
        OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("key", "3");
        params.put("to", "app");
        String url = Utils.getCompUrlFromParams(Global.SERVER_URL, params);
        Log.e("333", url);
        Request request = new Request.Builder().url(url).build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
//                mHandler.sendEmptyMessage(FAILURE);
                Message msg = new Message();
                msg.what = 2;
                hanler.sendMessage(msg);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String res = response.body().string().trim();
                Log.e("333", "res：" + res);
                analysisDate(1, res);
            }
        });

    }

    private void analysisDate(int j, String str) {
//        List<TableBean> mBeanList = new ArrayList<>();
        List<KaifubiaoBean> kaidus = new ArrayList<>();
        try {
//            for (int i = j; i < 15 + j; i++) {
//                JSONObject jsonObject = new JSONObject(str)
//                        .getJSONObject("game_" + i);
//                Logger.msg("处理的数据", i + "：" + jsonObject);
//                if (i > 1) {
//                    TableBean bean = new TableBean();
//                    //获取时间戳并将其转换为正常时间
//                    String d = jsonObject.getString("D");
//                    String format = "yyyy-MM-dd HH:mm:ss";
//                    SimpleDateFormat sdf = new SimpleDateFormat(format);
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(new Date());
//                    String data = sdf.format(new Date(Long.valueOf(d + "000")));
//
//                    data = data.split(" ")[1];
//                    bean.setData(data);
//                    bean.setGameName(jsonObject.getString("B"));//B代表游戏名
//                    bean.setGameArea(jsonObject.getString("C"));//A代表要开服的区
//                    bean.setWeek(jsonObject.getString("A"));//A代表星期
//
//                    String gameDataStr = jsonObject.getString("gamedata");
//                    if (!gameDataStr.equals("No search to the data")) {
//                        JSONObject gameDataObj = new JSONObject(gameDataStr);
//                        bean.setIconUrl(gameDataObj.getString("icon"));
//                        bean.setGameId(gameDataObj.getInt("id"));
//                    } else {
//                        bean.setIconUrl("");
//                        bean.setGameId(0);
//                    }
//
////                    if (isOneDay(bean.getWeek())) {
////                        Logger.msg("今日开服", str);
//                    mBeanList.add(bean);
////                    }
//                }
//            }
            kaidus = JsonUtil.parseList(str, KaifubiaoBean.class);
        } catch (Exception e) {
            Message msg = new Message();
            msg.what = 2;
            hanler.sendMessage(msg);
            e.printStackTrace();
        } finally {
            yesterday_pager = 15 + j;
//        ref_ly.setRefreshing(false);
            if (kaidus != null && kaidus.size() == 0) {
                Message msg = new Message();
                msg.what = 2;
                hanler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.obj = kaidus;
                msg.what = 1;
                hanler.sendMessage(msg);
            }

        }

    }


    private boolean isOneDay(String week) {
        String way = "星期日";
        if (mWay.equals("1")) {
            way = "星期日";
        } else if (mWay.equals("2")) {
            way = "星期一";
        } else if (mWay.equals("3")) {
            way = "星期二";
        } else if (mWay.equals("4")) {
            way = "星期三";
        } else if (mWay.equals("5")) {
            way = "星期四";
        } else if (mWay.equals("6")) {
            way = "星期五";
        } else if (mWay.equals("7")) {
            way = "星期六";
        }

        if (week.equals(way)) {
            return true;
        }
        return false;
    }

    /**
     * 隐藏对话框
     */
    private void hideDialog() {
        listView.refreshComplete();
        loading_ll.setVisibility(View.GONE);
    }

    private Toast toast = null;

    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
