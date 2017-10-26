package com.i76game.activity;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.bean.InformationRVBean;
import com.i76game.utils.GlideUtil;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.RetrofitUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/13.
 */

public class InformationActivity extends BaseActivity {

    private List<InformationRVBean.DataBean.NewsListBean> mInformationList;
    private InformationAdapter mAdapter;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_information;
    }

    @Override
    public void initView() {
        setToolbar("资讯中心",R.id.information_toolbar);
        XRecyclerView recyclerView= (XRecyclerView) findViewById(R.id.information_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InformationAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("appid", Global.appid);
        map.put("clientid", Global.clientid);
        map.put("agent", "");
        map.put("from", "3");
        map.put("page", "1");
        map.put("offset", "30");
        map.put("catalog","0");

        RetrofitUtil.getInstance().create(HttpServer.InformationService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InformationRVBean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull InformationRVBean informationRVBean) {
                        if (informationRVBean != null && informationRVBean.getCode() == 200) {
                            mInformationList = informationRVBean.getData().getNews_list();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showToast("暂无数据哦", Toast.LENGTH_SHORT);
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

    class InformationAdapter extends RecyclerView.Adapter<InformationHolder> {

        @Override
        public InformationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view= LayoutInflater.from(InformationActivity.this)
                   .inflate(R.layout.information_rv_layout,parent,false);
            return new InformationHolder(view);
        }

        @Override
        public void onBindViewHolder(InformationHolder holder, int position) {
            final InformationRVBean.DataBean.NewsListBean informationBean = mInformationList.get(position);
            GlideUtil.loadImage(InformationActivity.this, informationBean.getImg(), holder.mImageView,
                    R.mipmap.load_icon);

            holder.mTitleText.setText(informationBean.getTitle());
            holder.mTimeText.setText(informationBean.getPudate());
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id=informationBean.getId();
                    Intent intent=new Intent(InformationActivity.this,InformationContentActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mInformationList==null?0:mInformationList.size();
        }
    }


    class InformationHolder extends RecyclerView.ViewHolder{
        TextView mTimeText;
        ImageView mImageView;
        TextView mTitleText;
        public InformationHolder(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.information_rv_image);
            mTitleText= (TextView) itemView.findViewById(R.id.information_rv_title);
            mTimeText= (TextView) itemView.findViewById(R.id.information_rv_time);
        }
    }
}
