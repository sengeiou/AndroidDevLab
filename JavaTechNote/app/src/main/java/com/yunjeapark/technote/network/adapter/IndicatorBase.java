package com.yunjeapark.technote.network.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.yunjeapark.technote.R;
import com.yunjeapark.technote.utility.PixelUtils;

public abstract class IndicatorBase extends View {

    protected Context context;
    protected int itemCount;
    protected int selectPosition;
    protected int defaultColor;
    protected int selectedColor;
    protected int strokeColor;
    protected int strokeWidth;
    protected int gap;

    protected int radius;

    protected int lineWidth;
    protected int lineHeight;

    protected IndicatorType type;

    public IndicatorBase(Context context) {
        super(context);
        this.context = context;
        init(null);
    }


    public IndicatorBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        type = getType();

        defaultColor = context.getResources().getColor(R.color.gray);
        selectedColor = context.getResources().getColor(R.color.white);
        strokeColor = context.getResources().getColor(R.color.gray);
        strokeWidth = PixelUtils.dpToPx(context, 4);

        switch (type) {
            case LINE:
                lineWidth = PixelUtils.dpToPx(context, 16);
                lineHeight = lineWidth / 2;
                gap = lineWidth / 2;
                break;

            case CIRCLE:
                radius = PixelUtils.dpToPx(context, 6);
                gap = radius * 2;
                break;
        }


        if (attrs == null)
            return;


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorBase);
        defaultColor = typedArray.getColor(R.styleable.IndicatorBase_p_defaultColor, defaultColor);
        selectedColor = typedArray.getColor(R.styleable.IndicatorBase_p_selectedColor, selectedColor);
        strokeColor = typedArray.getColor(R.styleable.IndicatorBase_p_strokeColor, strokeColor);
        radius = typedArray.getDimensionPixelSize(R.styleable.IndicatorBase_p_radius, radius);
        lineWidth = typedArray.getDimensionPixelOffset(R.styleable.IndicatorBase_p_lineWidth, lineWidth);
        lineHeight = typedArray.getDimensionPixelSize(R.styleable.IndicatorBase_p_lineHeight, lineHeight);
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.IndicatorBase_p_strokeWidth, strokeWidth);
        gap = typedArray.getDimensionPixelSize(R.styleable.IndicatorBase_p_gap, gap);
    }

    protected abstract IndicatorType getType();

    public void setupWithViewPager(ViewPager pager) {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosition = position;
                invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        itemCount = pager.getAdapter().getCount();


        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        switch (type) {
            case LINE:
                layoutParams.width = ((lineWidth + gap) * itemCount) - gap + (strokeWidth * 2);
                layoutParams.height = lineHeight;
                break;
            case CIRCLE:
                layoutParams.width = (((radius * 2) + gap) * itemCount - gap) + (strokeWidth * 2);
                layoutParams.height = radius * 2 + (strokeWidth * 2);
                break;
        }
        setLayoutParams(layoutParams);
        invalidate();
    }

    protected Paint getFillPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    protected Paint getStrokePaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }
}
