package com.wcedla.driving_school.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class SplitLineGridview extends GridView {

    public SplitLineGridview(Context context) {
        super(context);
    }

    public SplitLineGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SplitLineGridview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SplitLineGridview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int column = getNumColumns();//列数
        int childCount = getChildCount();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);//这个就是设置分割线的颜色
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(2);
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);
            if ((i + 1) % column == 0) {//每一行最后一个
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), paint);
            } else if ((i + 1) > (childCount - (childCount % column))) {//最后一行的item
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), paint);
            } else {
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), paint);
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), paint);
            }
        }
    }
}