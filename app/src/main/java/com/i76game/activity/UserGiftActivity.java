package com.i76game.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.bean.UserGiftCodeBean;
import com.i76game.utils.GlideUtil;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.OkHttpUtil;
import com.i76game.view.LoadDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 我的礼包
 */
public class UserGiftActivity extends BaseActivity {

    private List<UserGiftCodeBean.DataBean.GiftListBean> mGiftList;
    private RecyclerView mRecyclerView;
    private UserGiftAdapter mAdapter;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_gift;
    }

    @Override
    public void initView() {

        setToolbar("我的礼包",R.id.gift_toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.gift_rv);
        mAdapter = new UserGiftAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        Map<String,String> map=new HashMap<>();
        map.put("appid",Global.appid);
        map.put("from",Global.from);
        map.put("clientid",Global.clientid);
        map.put("agent",Global.agent);
        request(map);
    }

    private  LoadDialog mLoadDialog;
    private void request(Map<String,String> map){
        mLoadDialog = new LoadDialog(this,true,"100倍加速中");
        mLoadDialog.show();

        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request=chain.request();
                        Request.Builder builder=request.newBuilder();
                        builder.addHeader("hs-token", OkHttpUtil.gethstoken());
                        builder.addHeader("timestamp", OkHttpUtil.Gettimestamp()+"");
                        return chain.proceed(builder.build());
                    }
                }).retryOnConnectionFailure(true).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Global.Hot_GAME_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        Observable<UserGiftCodeBean> observable = retrofit.create(
                HttpServer.UserGiftService.class).listResponse(map);
        observable.subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserGiftCodeBean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UserGiftCodeBean userGiftCodeBean) {
                        if (userGiftCodeBean != null && userGiftCodeBean.getCode()==200) {
                            mGiftList = userGiftCodeBean.getData().getGift_list();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showToast("暂无数据哦", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        hideDialog();
                    }
                });
    }


    /**
     * 隐藏对话框
     */
    private void hideDialog(){
        if (mLoadDialog!=null){
            mLoadDialog.dismiss();
        }
    }

    class UserGiftAdapter extends RecyclerView.Adapter<UserGiftHolder>{

        @Override
        public UserGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(UserGiftActivity.this).inflate(
                    R.layout.user_gift_rv_layout,parent,false);
            return new UserGiftHolder(view);
        }

        @Override
        public void onBindViewHolder(UserGiftHolder holder, final int position) {
            final UserGiftCodeBean.DataBean.GiftListBean bean = mGiftList.get(position);
            holder.mContent.setText(bean.getContent());
            holder.mTitle.setText(bean.getGiftname());
            holder.mGiftId.setText(bean.getCode());
            GlideUtil.loadImage(bean.getIcon(),holder.mImageResource,R.mipmap.load_icon);
            holder.mCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String giftCode = bean.getCode().trim();
                    ClipboardManager cmb = (ClipboardManager) getSystemService(
                            Context.CLIPBOARD_SERVICE);
                    cmb.setText(giftCode);
                    showToast("复制成功", Toast.LENGTH_SHORT);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGiftList == null?0:mGiftList.size();
        }
    }


    class UserGiftHolder extends RecyclerView.ViewHolder{
        ImageView mImageResource;
        TextView mTitle;
        TextView mContent;
        TextView mGiftId;
        Button mCopy;
        UserGiftHolder(View itemView) {
            super(itemView);
            mImageResource= (ImageView) itemView.findViewById(R.id.gift_rv_icon);
            mTitle= (TextView) itemView.findViewById(R.id.gift_rv_name);
            mContent= (TextView) itemView.findViewById(R.id.gift_rv_content);
            mGiftId= (TextView) itemView.findViewById(R.id.gift_rv_id);
            mCopy= (Button) itemView.findViewById(R.id.gift_rv_copy);
        }
    }


}
