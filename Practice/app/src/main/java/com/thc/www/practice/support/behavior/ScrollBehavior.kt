package com.thc.www.practice.support.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.View


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/14
 */
// 注意一下，带有参数的这个构造必须要重载，因为在CoordinatorLayout里利用反射去获取这个Behavior的时候就是拿的这个构造
class ScrollBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<NestedScrollView>(context, attrs) {
    // 这里的返回值表明这次滑动我们要不要关心，我们要关心什么样的滑动。这里关系的是y轴方向上的。
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: NestedScrollView,
                                     directTargetChild: View,
                                     target: View,
                                     axes: Int,
                                     type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    // 让child的scrollY的值等于目标的scrollY的值
    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout,
                                   child: NestedScrollView,
                                   target: View,
                                   dx: Int,
                                   dy: Int,
                                   consumed: IntArray,
                                   type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        val leftScrolled = target.scrollY
        child.scrollY = leftScrolled
    }

    // 直接将现在的y轴上的速度传递传递给child，让他fling起来
    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout,
                                  child: NestedScrollView,
                                  target: View,
                                  velocityX: Float,
                                  velocityY: Float): Boolean {
        child.fling(velocityY.toInt())
        return false
    }
}