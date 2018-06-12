package com.changsukuaidi.www.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.bean.RoutePoint;
import com.changsukuaidi.www.common.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 路线绘制 =/=
 * @Author zhouhao
 * @Date 2018/3/8
 * @Contact 605626708@qq.com
 */

public class RouteLayout extends View {

    public static int margin_15;
    public static int margin_30;
    public static int dimen_2;
    public static int dimen_4;
    public static int dimen_8;
    public static int dimen_10;

    public static int sp_15;
    public static int sp_13;

    public static int common_text;
    public static int common_text_gray;
    public static int theme_color;
    public static int divider_color;

    protected List<RoutePoint> points = new ArrayList<>();

    private Paint mCirclePaint = new Paint(); // 圆用画笔
    private Paint mTextPaint = new Paint();  // 文字用画笔
    private Paint mDashPaint = new Paint();  // 虚线用画笔
    private Path mPath = new Path();  // 虚线用path

    public RouteLayout(Context context) {
        super(context);
        init();
    }

    public RouteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RouteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // dimen initial
        margin_15 = getResources().getDimensionPixelSize(R.dimen.margin_small);
        margin_30 = DisplayUtils.dp2px(getContext(), 30);
        dimen_2 = DisplayUtils.dp2px(getContext(), 2);
        dimen_4 = DisplayUtils.dp2px(getContext(), 4);
        dimen_8 = DisplayUtils.dp2px(getContext(), 8);
        dimen_10 = DisplayUtils.dp2px(getContext(), 10);

        // sp initial
        sp_15 = DisplayUtils.sp2px(getContext(), 15);
        sp_13 = DisplayUtils.sp2px(getContext(), 13);

        // color initial
        common_text = getResources().getColor(R.color.common_text);
        common_text_gray = getResources().getColor(R.color.common_text_grey);
        theme_color = getResources().getColor(R.color.themeColor);
        divider_color = getResources().getColor(R.color.divider_color);

        // circle paint initial
        mCirclePaint.setStyle(Paint.Style.FILL);

        // text paint initial
        mTextPaint.setColor(common_text);

        // dash paint initial
        mDashPaint.setColor(divider_color);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(dimen_2);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{6, 4, 4, 4}, 2));

        mPath = new Path();
    }

    public void setRoute(List<RoutePoint> points) {
        this.points.clear();
        this.points.addAll(points);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 平移到 开始绘制点.
        canvas.translate(margin_15, margin_15);

        mPath.moveTo(0, dimen_4);
        mPath.lineTo(0, getHeight());


        for (int i = 0; i < points.size(); i++) {

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
