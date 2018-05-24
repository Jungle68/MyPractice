package com.thc.www.practice.support.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.util.AttributeSet
import android.view.View
import com.thc.www.practice.utils.AnimatorUtil


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class ZhihuFABBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {
    // 隐藏动画是否正在执行
    private var isAnimatingOut = false
    private var mOnStateChangedListener: OnStateChangedListener? = null

    fun setOnStateChangedListener(mOnStateChangedListener: OnStateChangedListener) {
        this.mOnStateChangedListener = mOnStateChangedListener
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild:
                                     View, target: View,
                                     axes: Int,
                                     type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout,
                                   child: FloatingActionButton,
                                   target: View,
                                   dx: Int,
                                   dy: Int,
                                   consumed: IntArray,
                                   type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (dy > 0 && !isAnimatingOut
                && child.visibility == View.VISIBLE) {// 手指上滑，隐藏FAB
            AnimatorUtil.scaleHide(child, listener)
            if (mOnStateChangedListener != null) {
                mOnStateChangedListener?.onChanged(false)
            }
        } else if (dy < 0 && child.visibility != View.VISIBLE) {
            AnimatorUtil.scaleShow(child, null)// 手指下滑，显示FAB
            if (mOnStateChangedListener != null) {
                mOnStateChangedListener?.onChanged(true)
            }
        }
    }

    private val listener = object : ViewPropertyAnimatorListener {
        override fun onAnimationStart(view: View) {
            isAnimatingOut = true
        }

        override fun onAnimationEnd(view: View) {
            isAnimatingOut = false
            view.visibility = View.INVISIBLE
        }

        override fun onAnimationCancel(arg0: View) {
            isAnimatingOut = false
        }
    }

    // 外部监听显示和隐藏。
    interface OnStateChangedListener {
        fun onChanged(isShow: Boolean)
    }

    companion object {
        fun <V : View> from(view: V): ZhihuFABBehavior {
            val params = view.layoutParams as? CoordinatorLayout.LayoutParams
                    ?: throw IllegalArgumentException("这个View不是CoordinatorLayout的子View")
            val behavior = params.behavior as? ZhihuFABBehavior
                    ?: throw IllegalArgumentException("这个View的Behavior不是ScaleDownShowBehavior")
            return behavior
        }
    }
}