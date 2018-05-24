package com.thc.www.p2p.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/17
 */
class NoTouchViewPager(context: Context, attributeSet: AttributeSet? = null)
    : ViewPager(context, attributeSet) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean = false
}