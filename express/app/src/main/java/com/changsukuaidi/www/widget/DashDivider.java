package com.changsukuaidi.www.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.common.utils.DisplayUtils;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class DashDivider extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private PathEffect effects = new DashPathEffect(new float[]{6, 4, 4, 4}, 2);

    public DashDivider(Context context) {
        super(context);
    }

    public DashDivider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashDivider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.divider_color));
        paint.setStrokeWidth(DisplayUtils.dp2px(getContext(),2));
        path.moveTo(0, 0);
        path.lineTo(0, getHeight());
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
