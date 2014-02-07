package com.jsdev.ruime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class VideoViewCustom extends VideoView {

    private int mForceHeight = 0;
    private int mForceWidth = 0;

    public VideoViewCustom(Context context) {
        super(context);
    }

    public VideoViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setDimensions(int w, int h) {
        this.mForceHeight = h;
        this.mForceWidth = w;

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        setMeasuredDimension(widthMeasureSpec>mForceWidth, mForceHeight);
//    }
}
