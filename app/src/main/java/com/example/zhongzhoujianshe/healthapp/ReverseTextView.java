package com.example.zhongzhoujianshe.healthapp;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


public class ReverseTextView extends android.support.v7.widget.AppCompatTextView {
    public ReverseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.scale(-1, 1, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
