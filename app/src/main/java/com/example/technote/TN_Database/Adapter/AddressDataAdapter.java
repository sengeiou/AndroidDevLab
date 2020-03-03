package com.example.technote.TN_Database.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technote.TN_Database.Data.AddressData;
import com.example.technote.R;

import java.util.ArrayList;

public class AddressDataAdapter extends RecyclerView.Adapter<AddressDataAdapter.CustomViewHolder> {

    private ArrayList<AddressData> mList = null;
    private Activity context = null;
    private MyRecyclerViewClickListener mListener;

    public AddressDataAdapter(Activity context, ArrayList<AddressData> list) {
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
        protected ImageView account_image;
        protected TextView name, phoneNumber;
        protected LinearLayout post;
        public CustomViewHolder(View view) {
            super(view);
            this.account_image = (ImageView) view.findViewById(R.id.image_account);
            this.name = (TextView) view.findViewById(R.id.text_address_book_name);
            this.phoneNumber = (TextView)view.findViewById(R.id.text_address_book_phone_number);
            this.post = (LinearLayout)view.findViewById(R.id.address_book_item_layout);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_address_book_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    // 뷰 홀더를 생성하는 부분
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.account_image.setImageResource(R.drawable.circle_account);
        viewholder.name.setText(mList.get(position).getName());
        viewholder.phoneNumber.setText(mList.get(position).getPhone_number());

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
        viewholder.account_image.setScaleType(ImageView.ScaleType.FIT_XY);

    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
