package com.yunjeapark.technote.network.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.yunjeapark.technote.R;
import com.yunjeapark.technote.network.BoardContent_Image;
import com.yunjeapark.technote.network.BoardContent_Image_ZoomIn;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by gdtbg on 2017-09-16.
 */

public class ImageViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> data;
    private boolean imageClick;

    public ImageViewPagerAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.adapter_image_viewpager,null);
        ImageView image_container = (ImageView) v.findViewById(R.id.image_container);
        image_container.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context)
                .load(data.get(position))
                .into(image_container);
        container.addView(v);
        imageClick = false;
        image_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BoardContent_Image.itemClick){
                    Intent imageZoomInIntent = new Intent(context.getApplicationContext(), BoardContent_Image_ZoomIn.class);
                    imageZoomInIntent.putExtra("position",BoardContent_Image.viewPagerPosition);
                    imageZoomInIntent.putExtra("data",BoardContent_Image.data);
                    context.startActivity(imageZoomInIntent);
                    imageClick = true;
                }
            }
        });
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}