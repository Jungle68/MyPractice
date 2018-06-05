package com.thc.www.practice.support.recyclerview.banner

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.util.DisplayMetrics
import android.view.View
import java.lang.ref.WeakReference

class BannerLayoutManager : LinearLayoutManager {
    private var mLinearSnapHelper: LinearSnapHelper
    private var recyclerView: RecyclerView
    private var mOnSelectedViewListener: OnSelectedViewListener? = null
    private var mRealCount: Int = 0
    private var mCurrentPosition = 0
    private var mHandler: TaskHandler
    private var mTimeDelayed: Long = 1000
    private var mOrientation: Int = 0
    private var mTimeSmooth = 150f

    constructor(context: Context, recyclerView: RecyclerView, realCount: Int) : super(context) {
        this.mLinearSnapHelper = LinearSnapHelper()
        this.mRealCount = realCount
        this.mHandler = TaskHandler(this)
        this.recyclerView = recyclerView
        this.orientation = LinearLayoutManager.HORIZONTAL
        this.mOrientation = LinearLayoutManager.HORIZONTAL
    }

    constructor(context: Context, recyclerView: RecyclerView, realCount: Int, orientation: Int) : super(context) {
        this.mLinearSnapHelper = LinearSnapHelper()
        this.mRealCount = realCount
        this.mHandler = TaskHandler(this)
        this.recyclerView = recyclerView
        this.orientation = orientation
        this.mOrientation = orientation
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        mLinearSnapHelper.attachToRecyclerView(view)
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
            // 返回：滑过1px时经历的时间(ms)。
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return mTimeSmooth / displayMetrics.densityDpi
            }
        }

        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }


    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {//滑动停止

            val view = mLinearSnapHelper.findSnapView(this)
            mCurrentPosition = getPosition(view!!)

            if (mOnSelectedViewListener != null)
                mOnSelectedViewListener!!.onSelectedView(view, mCurrentPosition % mRealCount)

            mHandler.setSendMsg(true)
            val msg = Message.obtain()
            mCurrentPosition++
            msg.what = mCurrentPosition
            mHandler.sendMessageDelayed(msg, mTimeDelayed)

        } else if (state == SCROLL_STATE_DRAGGING) {//拖动
            mHandler.setSendMsg(false)
        }
    }

    fun setTimeDelayed(timeDelayed: Long) {
        this.mTimeDelayed = timeDelayed
    }

    fun setTimeSmooth(timeSmooth: Float) {
        this.mTimeSmooth = timeSmooth
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        mHandler.setSendMsg(true)
        val msg = Message.obtain()
        msg.what = mCurrentPosition + 1
        mHandler.sendMessageDelayed(msg, mTimeDelayed)
    }

    fun setOnSelectedViewListener(listener: OnSelectedViewListener) {
        this.mOnSelectedViewListener = listener
    }


    /**
     * 停止时，显示在中间的View的监听
     */
    interface OnSelectedViewListener {
        fun onSelectedView(view: View, position: Int)
    }

    private class TaskHandler(bannerLayoutManager: BannerLayoutManager) : Handler() {
        private val mWeakBanner: WeakReference<BannerLayoutManager> = WeakReference(bannerLayoutManager)
        private var mSendMsg: Boolean = false

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            if (msg != null && mSendMsg) {
                val position = msg.what
                val bannerLayoutManager = mWeakBanner.get()
                bannerLayoutManager?.recyclerView?.smoothScrollToPosition(position)
            }
        }

        fun setSendMsg(sendMsg: Boolean) {
            this.mSendMsg = sendMsg
        }

    }

    companion object {
        private const val TAG = "BannerLayoutManager"
    }
}
