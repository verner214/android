package com.appkonst.repete;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lars on 2017-02-28.
 */

public class ScalingImageView extends ImageView {
    public ScalingImageView(Context context) {
        super(context);
    }

    public ScalingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //i widthMeasureSpec finns en flagga (mode) och en width (hittils uträknad).
//mode kan vara en av AT_MOST (width kan ändras till som mest X), EXCACTLY (width kan inte ändras) och UNSPECIFIED (width kan ändras).
//MeasureSpec.getSize(widthMeasureSpec) tar ut width widthMeasureSpec
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable mDrawable = getDrawable();
        if (mDrawable != null) {
            int mDrawableWidth = mDrawable.getIntrinsicWidth();
            int mDrawableHeight = mDrawable.getIntrinsicHeight();
            float actualAspect = (float) mDrawableWidth / (float) mDrawableHeight;

            // Assuming the width is ok, so we calculate the height.
            final int actualWidth = MeasureSpec.getSize(widthMeasureSpec);
            final int height = (int) (actualWidth / actualAspect);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
