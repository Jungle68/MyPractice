package com.changsukuaidi.www.widget.linner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.changsukuaidi.www.R;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class HomeHeaderLinear extends FrameLayout {

    private Paint paint = new Paint();
    private Path path = new Path();

    public HomeHeaderLinear(Context context) {
        super(context);
        init();
    }

    public HomeHeaderLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeHeaderLinear(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        // 启用onDraw();
        setWillNotDraw(false);

        paint.setColor(getResources().getColor(R.color.themeColor));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(0, 0);
        path.lineTo(getWidth(), 0);
        path.lineTo(0, getHeight());
        path.close();
        canvas.drawPath(path, paint);
    }
}
