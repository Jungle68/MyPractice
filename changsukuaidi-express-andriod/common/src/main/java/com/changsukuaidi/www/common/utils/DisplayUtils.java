package com.changsukuaidi.www.common.utils;

import android.content.Context;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

/**
 * @Describe
 * @Author thc
 * @Date 2017/11/30
 */

public class DisplayUtils {
    public static int getWindowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getWindowHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }

    public static int sp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, context.getResources()
                .getDisplayMetrics());
    }

    public static void hideSoftInput(Context context, IBinder windowToken) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
