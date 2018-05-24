package com.thc.www.myinfo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display

/**
 * Created by Weiping Huang at 23:40 on 2016/3/3
 * For Personal Open Source
 * Contact me at 2584541288@qq.com or nightonke@outlook.com
 * For more projects: https://github.com/Nightonke
 */

internal object DisplayUtil {
    private var screenWidth = -1
    private var screenHeight = -1

    /**
     * get the screen width in pixels
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        if (screenWidth == -1) {
            val display = (context as Activity).windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }
        return screenWidth
    }

    /**
     * get the screen height in pixels
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        if (screenHeight == -1) {
            val display = (context as Activity).windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }
        return screenHeight
    }

    /**
     * dp to px
     *
     * @param dp
     * @param mContext
     * @return
     */
    fun dp2px(dp: Int, mContext: Context): Int {
        val displayMetrics = mContext.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}
