package com.i76game.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.i76game.view.CustomLinearLayoutManager;
import com.i76game.view.GiftDialog;
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
 * Created by Administrator on 2017/11/6.
 */

public class GiftFragment extends Fragment {
    private View view;
    private List<GiftBean.DataBean.GiftListBean> mGiftList;
    private GiftListAdapter mAdapter;
    public XRecyclerView mRecyclerView;
    private int mPageIndex = 1;
    public CustomLinearLayoutManager linearLayoutManager;
    public String app_id;
    private View layoutNoData;
    private LinearLayout loading_ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gift_list_fragment, null);
        initView();
        return view;
    }


    public void initView() {
        mGiftList = new ArrayList<>();
        final ArrayMap<String, String> map = new ArrayMap<>();
        map.put("appid", app_id);
        map.put("clientid", Global.clientid);
        map.put("agent", Global.agent);
        map.put("from", Global.from);
        map.put("page", mPageIndex + "");
        map.put("offset", "30");

        layoutNoData = view.findViewById(R.id.layout_noData);
        loading_ll = (LinearLayout) view.findViewById(R.id.loading_ll);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.gift_list_rv);
        linearLayoutManager = new CustomLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        request(map);
    }

    private void request(Map<String, String> map) {
        //每一次请求都加一，下次加载就是下一页
        mPageIndex++;
        layoutNoData.setVisibility(View.GONE);
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
                           if (mGiftList.size() == 0){
                               layoutNoData.setVisibility(View.VISIBLE);
                           }
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
            View view = LayoutInflater.from(getActivity()).inflate(
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
            GlideUtil.loadImage(getActivity(), giftListBean.getIcon(), holder.mImageResource,
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
        GiftDialog dialog = new GiftDialog(getActivity(), arrayList);
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
        loading_ll.setVisibility(View.GONE);
        mRecyclerView.loadMoreComplete();
        mRecyclerView.refreshComplete();
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
