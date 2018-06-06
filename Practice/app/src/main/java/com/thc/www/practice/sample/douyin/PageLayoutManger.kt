package com.thc.www.practice.sample.douyin

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @Describe
 * @Author thc
 * @Date  2018/6/5
 */
class PageLayoutManger(context: Context, orientation: Int = LinearLayoutManager.VERTICAL, reverseLayout: Boolean = false)
    : LinearLayoutManager(context, orientation, reverseLayout) {
    private val mPagerSnapHelper: PagerSnapHelper = PagerSnapHelper()
    private var mOnViewPagerListener: OnViewPagerListener? = null
    private var mRecyclerView: RecyclerView? = null
    private var mDrift: Int = 0 //位移，用来判断移动方向

    private val mChildAttachStateChangeListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View?) {
        }

        override fun onChildViewDetachedFromWindow(view: View?) {
            if (mDrift >= 0) {
                if (mOnViewPagerListener != null)
                    mOnViewPagerListener?.onPageRelease(true, getPosition(view))
            } else {
                if (mOnViewPagerListener != null)
                    mOnViewPagerListener?.onPageRelease(false, getPosition(view))
            }
        }
    }

    /**
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     * @param state
     */
    override fun onScrollStateChanged(state: Int) {
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                val viewIdle = mPagerSnapHelper.findSnapView(this)
                val positionIdle = getPosition(viewIdle)
                if (mOnViewPagerListener != null && childCount == 1) {
                    mOnViewPagerListener?.onPageSelected(positionIdle, positionIdle == itemCount - 1)
                }
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                val viewDrag = mPagerSnapHelper.findSnapView(this)
                val positionDrag = getPosition(viewDrag)
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                val viewSettling = mPagerSnapHelper.findSnapView(this)
                val positionSettling = getPosition(viewSettling)
            }
        }
    }

    /**
     * 布局完成后调用
     * @param state
     */
    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        if (mOnViewPagerListener != null)
            mOnViewPagerListener?.onLayoutComplete()
    }

    /**
     * 监听竖直方向的相对偏移量
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        this.mDrift = dy
        return super.scrollVerticallyBy(dy, recycler, state)
    }


    /**
     * 监听水平方向的相对偏移量
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        this.mDrift = dx
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    /**
     * 设置监听
     * @param listener
     */
    fun setOnViewPagerListener(listener: OnViewPagerListener) {
        this.mOnViewPagerListener = listener
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        mPagerSnapHelper.attachToRecyclerView(view)
        this.mRecyclerView = view
        mRecyclerView?.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener)
    }
}