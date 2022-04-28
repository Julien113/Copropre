package com.copropre.common.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquaredGridLayout extends RelativeLayout {

    public SquaredGridLayout(Context context) {
        super(context);
    }

    public SquaredGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquaredGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMesured, int heightMesured) {
        super.onMeasure(widthMesured, widthMesured);
    }
}
