package com.i76game.activity;

import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.i76game.R;
import com.i76game.adapter.FenleiAdapter;
import com.i76game.bean.TypeBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/14.
 */

public class FenLeiActivity extends BaseActivity {
    private ListView fenlei_list;
    private List<TypeBean.DataBean> dataBeens;
    private FenleiAdapter adapter;
    private Toolbar fenlei_toolbar;
    @Override
    protected int setLayoutResID() {
        return R.layout.fenlei_activity;
    }

    @Override
    public void initView() {
        setToolbar("分类", R.id.fenlei_toolbar);
        fenlei_toolbar = (Toolbar) findViewById(R.id.fenlei_toolbar);
        fenlei_list = (ListView) findViewById(R.id.fenlei_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fenlei_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("clientid", Global.clientid);
        map.put("appid", Global.appid);
        map.put("agent", Global.agent);
        map.put("from", Global.from);
        RetrofitUtil.getInstance()
                .create(HttpServer.TypeService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TypeBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull TypeBean typeBean) {
                        if (typeBean.getData() != null && typeBean.getCode() == 200) {
                            dataBeens = typeBean.getData();
                            adapter = new FenleiAdapter(dataBeens, FenLeiActivity.this);
                            fenlei_list.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
//                        mPopupGrid.setAdapter(new ArrayAdapter<String>(GameListActivity.this
//                                , android.R.layout.simple_list_item_1, mTypes));
                    }
                });
    }
}
