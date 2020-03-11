package com.example.technote.TN_Network.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technote.TN_Network.Data.Network_Board_ImageListData;
import com.example.technote.R;
import com.loopj.android.image.SmartImageView;

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

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected SmartImageView title_image;
        protected TextView title;
        protected TextView subject;
        protected TextView price;
        protected RelativeLayout layout_image_list;

        public CustomViewHolder(View view) {
            super(view);
            this.title_image = (SmartImageView) view.findViewById(R.id.title_image_view);
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

        viewholder.title_image.setImageUrl(mList.get(position).getPhoto_url_1());
        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.subject.setText(mList.get(position).getSubject());
        viewholder.price.setText(mList.get(position).getPrice());
        viewholder.title_image.setScaleType(ImageView.ScaleType.FIT_XY);

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
