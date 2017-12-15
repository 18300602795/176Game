package com.i76game.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.activity.CategoryActivity;
import com.i76game.activity.GameInfoActivity;
import com.i76game.bean.HomeRVBean;
import com.i76game.bean.TypeBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.RetrofitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/14.
 */

public class FenleiAdapter extends BaseAdapter {
    private List<TypeBean.DataBean> dataBeens;
    private Context context;
    private Map<String, String> mMap = new HashMap<>();
    private List<HomeRVBean.DataBean.GameListBean> mGameList;
    private FenleiGrideAdapter mAdapter;

    public FenleiAdapter(List<TypeBean.DataBean> dataBeens, Context context) {
        this.dataBeens = dataBeens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataBeens.size();
    }

    @Override
    public Object getItem(int i) {
        return dataBeens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.fenlei_item, null);
            holder.type_tv = (TextView) view.findViewById(R.id.type_tv);
            holder.more_ll = (LinearLayout) view.findViewById(R.id.more_ll);
            holder.fenlei_gv = (GridView) view.findViewById(R.id.fenlei_gv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.type_tv.setText(dataBeens.get(i).getTypename());
        request(holder, addTypeMap(String.valueOf(dataBeens.get(i).getTypeid())));
        holder.fenlei_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeRVBean.DataBean.GameListBean gameListBean = (HomeRVBean.DataBean.GameListBean) holder.fenlei_gv.getAdapter().getItem(i);
                Global.drawable = null;
                Intent intent = new Intent(context, GameInfoActivity.class);
                intent.putExtra(Global.GAME_ID, gameListBean.getGameid());
                context.startActivity(intent);
            }
        });
        holder.more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, CategoryActivity.class);
                intent.putExtra("category_name", dataBeens.get(i).getTypename());
                intent.putExtra("category_id", String.valueOf(dataBeens.get(i).getTypeid()));
                context.startActivity(intent);
            }
        });
        return view;
    }

    private void request(final ViewHolder holder, final Map<String, String> map) {
        RetrofitUtil.getInstance()
                .create(HttpServer.HotService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeRVBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HomeRVBean homeRVBean) {
                        if (homeRVBean != null && homeRVBean.getCode() == 200) {
                            mGameList = homeRVBean.getData().getGame_list();
                            mAdapter = new FenleiGrideAdapter(mGameList, context);
                            holder.fenlei_gv.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    //请求类型的map
    private Map<String, String> addTypeMap(String typeId) {
        mMap.clear();
        mMap.put("appid", Global.appid);
        mMap.put("clientid", Global.clientid);
        mMap.put("agent", Global.agent);
        mMap.put("from", Global.from);
        mMap.put("page", "1");
        mMap.put("type", typeId);
        mMap.put("offset", "4");
        return mMap;
    }

    class ViewHolder {
        TextView type_tv;
        LinearLayout more_ll;
        GridView fenlei_gv;
    }

}
