package com.thc.www.practice.widget

import android.content.Context
import android.support.v4.view.*
import android.util.AttributeSet
import android.view.MotionEvent


/**
 * @Describe
 * @Author thc
 * @Date  2018/6/8
 */
class MyViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs), NestedScrollingChild {
    private var mChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)
    private var mNestedOffsetY = 0F
    private var mLastY = 0F
    private val mScrollOffset = IntArray(2)
    private val mScrollConsumed = IntArray(2)

    init {
        isNestedScrollingEnabled = true
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun startNestedScroll(axes: Int) = mChildHelper.startNestedScroll(axes)

    override fun stopNestedScroll() = mChildHelper.stopNestedScroll()

    override fun hasNestedScrollingParent() = mChildHelper.hasNestedScrollingParent()

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?) =
            mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?) =
            mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean) =
            mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float) = mChildHelper.dispatchNestedPreFling(velocityX, velocityY)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(event)

        val y = event.y
        event.offsetLocation(0F, mNestedOffsetY)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastY = y
                mNestedOffsetY = 0F
            }
            MotionEvent.ACTION_MOVE -> {
                var dy = mLastY - y
                val oldY = scrollY
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                if (dispatchNestedPreScroll(0, dy.toInt(), mScrollConsumed, mScrollOffset)) {
                    dy -= mScrollConsumed[1]
                    event.offsetLocation(0F, -mScrollOffset[1].toFloat())
                    mNestedOffsetY += mScrollOffset[1]
                }
                mLastY = y - mScrollOffset[1]
                if (dy < 0) {
                    val newScrollY = Math.max(0F, oldY + dy)
                    dy -= newScrollY - oldY
                    if (dispatchNestedScroll(0, (newScrollY - dy).toInt(), 0, dy.toInt(), mScrollOffset)) {
                        event.offsetLocation(0F, mScrollOffset[1].toFloat())
                        mNestedOffsetY += mScrollOffset[1]
                        mLastY -= mScrollOffset[1]
                    }
                }
                stopNestedScroll()
            }
            MotionEvent.ACTION_UP -> {
                stopNestedScroll()
            }
            MotionEvent.ACTION_CANCEL -> {
                stopNestedScroll()
            }
        }
        return super.onTouchEvent(event)
    }
}