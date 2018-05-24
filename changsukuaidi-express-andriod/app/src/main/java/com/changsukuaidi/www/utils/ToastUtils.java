package com.changsukuaidi.www.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.changsukuaidi.www.base.BaseApplication;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/18
 */

public class ToastUtils {
    private static Toast mToast;

    public static void showToast(String text) {
        showToast(BaseApplication.getApplication(), Toast.LENGTH_SHORT, text);
    }

    public static void showToast(int resID) {
        showToast(BaseApplication.getApplication(), Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(Context ctx, int resID) {
        showToast(ctx, Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_SHORT, text);
    }

    public static void showLongToast(Context ctx, int resID) {
        showToast(ctx, Toast.LENGTH_LONG, resID);
    }


    public static void showLongToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_LONG, text);
    }

    public static void showToast(Context ctx, int duration, int resID) {
        showToast(ctx, duration, ctx.getString(resID));
    }

    /**
     * Toast一个图片
     */
    public static Toast showToastImage(Context ctx, int resID) {
        final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        View mNextView = toast.getView();
        if (mNextView != null)
            mNextView.setBackgroundResource(resID);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    public static void showToast(final Context ctx, final int duration,
                                 final String text) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, text, duration);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 在UI线程运行弹出
     */
    public static void showToastOnUiThread(final Activity ctx, final String text) {
        if (ctx != null) {
            ctx.runOnUiThread(() -> showToast(ctx, text));
        }
    }
}
