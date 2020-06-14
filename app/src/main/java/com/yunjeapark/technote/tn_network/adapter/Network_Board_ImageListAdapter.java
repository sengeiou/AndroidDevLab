package com.yunjeapark.technote.tn_network.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yunjeapark.technote.R;
import com.yunjeapark.technote.tn_network.data.Network_Board_ImageListData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Network_Board_ImageListAdapter extends RecyclerView.Adapter<Network_Board_ImageListAdapter.CustomViewHolder> {

    private ArrayList<Network_Board_ImageListData> mList = null;
    private Activity context = null;
    private MyRecyclerViewClickListener mListener;

    public Network_Board_ImageListAdapter(Activity context, ArrayList<Network_Board_ImageListData> list) {
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

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView title_image;
        public TextView title;
        private TextView subject;
        public TextView price;
        private RelativeLayout layout_image_list;

        private CustomViewHolder(View view) {
            super(view);
            this.title_image = (ImageView) view.findViewById(R.id.title_image_view);
            this.title = (TextView) view.findViewById(R.id.title_text_view);
            this.subject = (TextView) view.findViewById(R.id.subject_text_view);
            this.price = (TextView) view.findViewById(R.id.price_text_view);
            this.layout_image_list = (RelativeLayout)view.findViewById(R.id.layout_image_list);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_board_image_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    // 뷰 홀더를 생성하는 부분
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        Log.d("onBindViewHolder","In onBindViewHolder");
        Picasso.with(context)
                .load(mList.get(position).getPhoto_url_1())
                .resize(250,250)
                .into(viewholder.title_image);
        //viewholder.title_image.setImageUrl(mList.get(position).getPhoto_url_1());
        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.subject.setText(mList.get(position).getSubject());
        viewholder.price.setText(mList.get(position).getPrice());

        //레이아웃 클릭 이벤트
        if(mListener != null) {
            final int pos = position;
            viewholder.layout_image_list.setOnClickListener(new View.OnClickListener() {
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