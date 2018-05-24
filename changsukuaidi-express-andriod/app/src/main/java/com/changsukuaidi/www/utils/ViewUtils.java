package com.changsukuaidi.www.utils;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/25
 */

import android.view.View;

/**
 * Created by MQ on 2017/5/3.
 */

public class ViewUtils {
    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }
}