package com.thc.www.practice.utils

import android.content.Context
import android.util.TypedValue

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/22
 */
object DisplayUtil {
    fun dp2px(value: Float, context: Context?) =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context!!.resources.displayMetrics)
}