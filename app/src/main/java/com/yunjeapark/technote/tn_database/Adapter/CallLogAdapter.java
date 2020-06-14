package com.yunjeapark.technote.tn_database.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yunjeapark.technote.tn_database.Data.CallLogData;
import com.yunjeapark.technote.R;

import java.util.ArrayList;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CustomViewHolder> {

    private ArrayList<CallLogData> mList = null;
    private Activity context = null;
    private MyRecyclerViewClickListener mListener;

    public CallLogAdapter(Activity context, ArrayList<CallLogData> list) {
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
        protected ImageView call_type_image;
        protected TextView name, date;
        protected LinearLayout post;
        public CustomViewHolder(View view) {
            super(view);
            this.call_type_image = (ImageView) view.findViewById(R.id.call_type);
            this.name = (TextView) view.findViewById(R.id.item_call_log_name);
            this.date = (TextView)view.findViewById(R.id.item_call_log_date);
            this.post = (LinearLayout)view.findViewById(R.id.item_call_log_layout);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_call_log, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    // 뷰 홀더를 생성하는 부분
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        if(mList.get(position).getType().equals("receive")){
            viewholder.call_type_image.setImageResource(R.drawable.received);
        }else if(mList.get(position).getType().equals("sent")){
            viewholder.call_type_image.setImageResource(R.drawable.sent);
        }else if(mList.get(position).getType().equals("missed")){
            viewholder.call_type_image.setImageResource(R.drawable.missed);
        }else if(mList.get(position).getType().equals("cancel")){
            viewholder.call_type_image.setImageResource(R.drawable.cancelled);
        }

        if(mList.get(position).getName().equals("null")){
            viewholder.name.setText(mList.get(position).getPhoneNumber());
        }else{
            viewholder.name.setText(mList.get(position).getName());
        }
        viewholder.date.setText(mList.get(position).getDate());

        //클릭 이벤트
        if(mListener != null) {
            final int pos = position;
            viewholder.post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                }
            });
        }
        viewholder.call_type_image.setScaleType(ImageView.ScaleType.FIT_XY);

    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
