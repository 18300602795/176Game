package com.i76game.activity;

import android.os.Build;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.bean.GiftBean;
import com.i76game.utils.GlideUtil;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.SharePrefUtil;
import com.i76game.utils.Utils;
import com.i76game.view.GiftDialog;
import com.i76game.view.LoadDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 礼包中心
 */

public class GiftListActivity extends BaseActivity {

    private List<GiftBean.DataBean.GiftListBean> mGiftList;
    private GiftListAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private int mPageIndex = 1;
    private LoadDialog mLoadDialog;
    private Toolbar gift_list_toolbar;

    @Override
    protected int setLayoutResID() {
        return R.layout.gift_list_activity;
    }

    @Override
    public void initView() {
        setToolbar("礼包中心", R.id.gift_list_toolbar);
        gift_list_toolbar = (Toolbar) findViewById(R.id.gift_list_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            gift_list_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        mGiftList = new ArrayList<>();
        final ArrayMap<String, String> map = new ArrayMap<>();
        map.put("appid", Global.appid);
        map.put("clientid", Global.clientid);
        map.put("agent", Global.agent);
        map.put("from", Global.from);
        map.put("page", mPageIndex + "");
        map.put("offset", "30");


        mRecyclerView = (XRecyclerView) findViewById(R.id.gift_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPageIndex = 1;
                map.put("page", mPageIndex + "");
                request(map);
            }

            @Override
            public void onLoadMore() {
                map.put("page", mPageIndex + "");
                request(map);
            }
        });

        mAdapter = new GiftListAdapter();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        request(map);
    }

    private void request(Map<String, String> map) {
        //每一次请求都加一，下次加载就是下一页
        mPageIndex++;
        if (mLoadDialog == null) {
            mLoadDialog = new LoadDialog(this, true, "100倍加速中");
            mLoadDialog.show();
        }
        RetrofitUtil.getInstance()
                .create(HttpServer.GiftService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GiftBean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull GiftBean giftBean) {
                        if (giftBean != null && giftBean.getCode() == 200) {
                            mGiftList.addAll(giftBean.getData().getGift_list());
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

    class GiftListAdapter extends RecyclerView.Adapter<GiftListHolder> {

        @Override
        public GiftListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(GiftListActivity.this).inflate(
                    R.layout.gift_list_rv_layout, parent, false);
            return new GiftListHolder(view);
        }

        @Override
        public void onBindViewHolder(GiftListHolder holder, final int position) {
            final GiftBean.DataBean.GiftListBean giftListBean = mGiftList.get(position);
            float i = (float) giftListBean.getRemain() / (float) giftListBean.getTotal() * 100;
            holder.mProgressText.setText("(剩余" + (int) (i + 0.5) + "%)");
            holder.mProgressBar.setProgress((int) (i + 0.5));
            holder.mTitle.setText(giftListBean.getGiftname());
            holder.mLength.setText("礼包数量：" + giftListBean.getTotal());
//            GlideUtil.loadImage(giftListBean.getIcon(), holder.mImageResource, R.mipmap.load_icon);
            GlideUtil.loadImage(GiftListActivity.this, giftListBean.getIcon(), holder.mImageResource,
                    R.mipmap.load_icon);
            holder.mGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isLogin = SharePrefUtil.getBoolean(MyApplication.getContextObject(), SharePrefUtil.KEY.FIRST_LOGIN, true);   //判断用户有没有登录
                    if (isLogin) {
                        showToast("要先登录哦", Toast.LENGTH_SHORT);
                    } else {
                        requestGetGift(giftListBean.getGiftid(), giftListBean.getContent());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGiftList == null ? 0 : mGiftList.size();
        }
    }

    private void showDialog(ArrayList<String> arrayList) {
        GiftDialog dialog = new GiftDialog(GiftListActivity.this, arrayList);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void requestGetGift(long giftid, final String giftContent) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("giftid", giftid + "");
        OkHttpUtil.postFormEncodingdata(Global.GET_GIFT, false, map, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                try {
                    parseProveJson(res, giftContent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //解析点击领取后返回的数据
    private void parseProveJson(String res, String giftContent) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        int code = jsonObject.getInt("code");
        Message message = Message.obtain();
        String msg;
        String giftCode = "";
        if (code >= 200 && code <= 250) {
            msg = jsonObject.getString("msg");
            String data = jsonObject.getString("data");
            jsonObject = new JSONObject(data);
            giftCode = jsonObject.getString("giftcode");
        } else {
            message.arg1 = 404;
            message.obj = jsonObject.getString("msg");
            msg = jsonObject.getString("msg");
        }

        final String[] strings = {msg, giftCode, giftContent};
        Observable<String> observable = Observable.fromArray(strings);
        observable.subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    ArrayList<String> mArrayList = new ArrayList<>();

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        mArrayList.add(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        showDialog(mArrayList);
                    }
                });
    }

    class GiftListHolder extends RecyclerView.ViewHolder {
        ImageView mImageResource;
        TextView mTitle;
        TextView mLength;
        ProgressBar mProgressBar;
        Button mGet;
        TextView mProgressText;

        GiftListHolder(View itemView) {
            super(itemView);
            mImageResource = (ImageView) itemView.findViewById(R.id.gift_list_rv_image);
            mTitle = (TextView) itemView.findViewById(R.id.gift_list_rv_name);
            mLength = (TextView) itemView.findViewById(R.id.gift_list_rv_length);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.gift_list_rv_progress);
            mGet = (Button) itemView.findViewById(R.id.gift_list_rv_get);
            mProgressText = (TextView) itemView.findViewById(R.id.gift_list_rv_progress_text);
        }
    }

    /**
     * 隐藏对话框
     */
    private void hideDialog() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
        mRecyclerView.loadMoreComplete();
        mRecyclerView.refreshComplete();
    }
}
