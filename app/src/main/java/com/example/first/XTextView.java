package com.example.first;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class XTextView extends AppCompatTextView {

    private boolean drawX = false;
    private Paint paint;

    public XTextView(Context context) {
        super(context);
        init();
    }

    public XTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawX) {
            int width = getWidth();
            int height = getHeight();
            canvas.drawLine(0, 0, width, height, paint);
            canvas.drawLine(width, 0, 0, height, paint);
        }
    }

    public void setDrawX(boolean drawX) {
        this.drawX = drawX;
        invalidate(); // 重新绘制视图
    }
}
