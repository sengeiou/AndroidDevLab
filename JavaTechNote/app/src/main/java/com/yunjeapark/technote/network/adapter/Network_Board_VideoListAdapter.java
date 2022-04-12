package com.yunjeapark.technote.network.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yunjeapark.technote.R;
import com.yunjeapark.technote.network.data.Network_Board_VideoListData;

import java.util.ArrayList;

public class Network_Board_VideoListAdapter extends RecyclerView.Adapter<Network_Board_VideoListAdapter.CustomViewHolder> {

    private ArrayList<Network_Board_VideoListData> mList = null;
    private Activity context = null;
    private MyRecyclerViewClickListener mListener;

    public Network_Board_VideoListAdapter(Activity context, ArrayList<Network_Board_VideoListData> list) {
        this.context = context;
        this.mList = list;

    }
    public interface MyRecyclerViewClickListener {
        // 아이템 클릭
        void onItemClicked(int position);
    }

    public void setOnClickListener(MyRecyclerViewClickListener listener) {
        mListener = listener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView title_image;
        protected TextView title;
        protected RelativeLayout post;

        public CustomViewHolder(View view) {
            super(view);
            this.title_image = (ImageView) view.findViewById(R.id.title_image_view);
            this.title = (TextView) view.findViewById(R.id.title_text_view);
            this.post = (RelativeLayout)view.findViewById(R.id.post);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_board_video_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    // 뷰 홀더를 생성하는 부분
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        Picasso.with(context)
                .load(mList.get(position).getThumbnail_url())
                .resize(250,250)
                .into(viewholder.title_image);
        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.title_image.setScaleType(ImageView.ScaleType.FIT_XY);

        if(mListener != null) {
            final int pos = position;
            viewholder.post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
