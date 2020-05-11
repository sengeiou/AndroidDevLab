package com.yunjeapark.technote.TN_UI.ScrollingGalleryControls;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

import recycler.coverflow.CoverFlowLayoutManager;
import recycler.coverflow.RecyclerCoverFlow;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ScrollingGalleryControls extends AppCompatActivity
        implements SGCAdapter.onItemListener, CoverFlowLayoutManager.OnSelected{
    private RecyclerCoverFlow mList;
    private SGCAdapter sgcAdapter;
    private int PREVIOUS_FOCUS_POSITION = 2;
    private int CURRENT_FOCUS_POSITION;
    private boolean firstFocus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_scrolling_gallery_controls);
        initList();
    }

    private void initList() {
        sgcAdapter = new SGCAdapter(this,this);
        mList = (RecyclerCoverFlow) findViewById(R.id.list);
        mList.setFlatFlow(false); // 나란히 배열할지 설정, false : 겹치게 배열됨.
        //mList.setGreyItem(true); //회색조 그라디언트 설정
        //mList.setAlphaItem(true); //반투명 그라디언트 설정
        mList.setAdapter(sgcAdapter);
        mList.scrollToPosition(2);
        mList.setOnItemSelectedListener(this);
        firstFocus = true;

    }

    @Override
    public void clickItem(int pos) {
        mList.smoothScrollToPosition(pos);
    }

    @Override
    public void onItemSelected(int position) {
        ((TextView)findViewById(R.id.index)).setText((position+1)+"/"+mList.getLayoutManager().getItemCount());
    }

    @Override
    public void onAnimationStart(int position) { // 직접 만든 리스너
        Log.d("onAnimation","onAnimation");

        mList.getChildAt(PREVIOUS_FOCUS_POSITION).setSelected(false);
        mList.getChildAt(position).setSelected(true);
        PREVIOUS_FOCUS_POSITION = position;
    }
}
