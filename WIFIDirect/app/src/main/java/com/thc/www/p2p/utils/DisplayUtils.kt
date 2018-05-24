package com.thc.www.p2p.utils

import android.content.Context
import android.os.IBinder
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/9
 */
object DisplayUtils {
    fun getWindowWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getWindowHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun dp2px(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources
                .displayMetrics).toInt()
    }

    fun dp2px(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources
                .displayMetrics).toInt()
    }

    fun sp2px(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp.toFloat(), context.resources
                .displayMetrics).toInt()
    }

    fun hideSoftInput(context: Context, windowToken: IBinder) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}