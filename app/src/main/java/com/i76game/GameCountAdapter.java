package com.i76game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.i76game.utils.GlideUtil;
import com.i76game.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/9.
 */

public class GameCountAdapter extends RecyclerView.Adapter<GameCountAdapter.GameCountHolder> {

    private ArrayList<String> mImageUrls;
    private Context mContext;

    public GameCountAdapter(Context context){
        mContext=context;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
    }

    @Override
    public GameCountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext)
                .inflate(R.layout.game_content_rv_layout,parent,false);
        return new GameCountHolder(view);
    }

    @Override
    public void onBindViewHolder(GameCountHolder holder, final int position) {
        LogUtils.i("url：" + mImageUrls.get(position));
        GlideUtil.loadImage(mImageUrls.get(position),holder.mImageView,R.mipmap.load_icon_vertical);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImageUrls==null?0:mImageUrls.size();
    }

    class GameCountHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public GameCountHolder(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.game_content_rv_image);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
