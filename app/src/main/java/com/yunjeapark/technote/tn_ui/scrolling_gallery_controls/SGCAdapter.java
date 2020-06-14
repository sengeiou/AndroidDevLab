package com.yunjeapark.technote.tn_ui.scrolling_gallery_controls;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yunjeapark.technote.R;

/**
 * Created by chenxiaoping on 2017/3/28.
 */

public class SGCAdapter extends RecyclerView.Adapter<SGCAdapter.ViewHolder>  {

    private Context mContext;
    private int[] mIcons_d = {R.drawable.control_icon01_d,R.drawable.control_icon02_d,R.drawable.control_icon03_d,
            R.drawable.control_icon04_d, R.drawable.control_icon05_d};
    private onItemListener clickCb;
    private String s;
    public SGCAdapter(Context c) {
        mContext = c;
    }

    public SGCAdapter(Context c, onItemListener cb) {
        mContext = c;
        clickCb = cb;
    }

    public void setOnClickLstn(onItemListener cb) {
        this.clickCb = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext).load(mIcons_d[position % mIcons_d.length])
                .into(holder.img);
        Log.d("YJP",String.valueOf(position % mIcons_d.length));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCb != null) {
                    clickCb.clickItem(position);
                }
            }
        });
        if(position == 2){
            holder.itemView.setSelected(true);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);

        }
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }
    interface onItemListener {
        void clickItem(int pos);
    }

}
