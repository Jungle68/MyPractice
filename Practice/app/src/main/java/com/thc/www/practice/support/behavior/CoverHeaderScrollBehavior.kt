package com.thc.www.practice.support.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/14
 */
class CoverHeaderScrollBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    override fun onLayoutChild(parent: CoordinatorLayout?, child: View?, layoutDirection: Int): Boolean {
        val params = child?.layoutParams as CoordinatorLayout.LayoutParams
        if (params.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            child.layout(0, 0, parent!!.width, parent.height)
            child.translationY = 450F
            return true
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: View,
                                     directTargetChild: View,
                                     target: View,
                                     axes: Int,
                                     type: Int): Boolean = axes == ViewCompat.SCROLL_AXIS_VERTICAL

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout,
                                   child: View,
                                   target: View,
                                   dx: Int,
                                   dy: Int,
                                   consumed: IntArray,
                                   type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (dy < 0) return
        val transY = child.translationY - dy
        if (transY in 0..450) {
            child.translationY = transY
            consumed[1] = dy
        }
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: View,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int,
                                type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyUnconsumed > 0) return
        val transY = child.translationY - dyUnconsumed
        if (transY in 0..450)
            child.translationY = transY
    }
}