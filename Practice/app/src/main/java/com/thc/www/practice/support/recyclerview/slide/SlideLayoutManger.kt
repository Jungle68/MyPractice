package com.thc.www.practice.support.recyclerview.slide

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/30
 */
class SlideLayoutManger(recyclerView: RecyclerView, itemTouchHelper: ItemTouchHelper) : RecyclerView.LayoutManager() {
    companion object {
        const val SLIDING_NONE = 1
        const val SLIDING_LEFT = 1 shl 2
        const val SLIDING_RIGHT = 1 shl 3
        const val SLIDED_LEFT = 1
        const val SLIDED_RIGHT = 1 shl 2
        const val DEFAULT_SHOW_ITEM = 3
        const val DEFAULT_SCALE = 0.1F
        const val DEFAULT_TRANSLATE_Y = 14
        const val DEFAULT_ROTATE_DEGREE = 15F
    }

    private val mOnTouchListener = View.OnTouchListener { v, event ->
        val childViewHolder = mRecyclerView.getChildViewHolder(v)
        if (event.action == MotionEvent.ACTION_DOWN) {
            mItemTouchHelper.startSwipe(childViewHolder)
        }
        false
    }

    private val mRecyclerView: RecyclerView
    private val mItemTouchHelper: ItemTouchHelper

    init {
        this.mRecyclerView = checkIsNull(recyclerView)
        this.mItemTouchHelper = checkIsNull(itemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    override fun generateDefaultLayoutParams() =
            RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    private fun <T> checkIsNull(t: T?): T {
        if (t == null) {
            throw NullPointerException()
        }
        return t
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler)
        val itemCount = itemCount
        if (itemCount > DEFAULT_SHOW_ITEM) {
            for (position in DEFAULT_SHOW_ITEM downTo 0) {
                val view = recycler!!.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 5 + getDecoratedMeasuredHeight(view))

                when {
                    position == DEFAULT_SHOW_ITEM -> {
                        view.scaleX = 1 - (position - 1) * DEFAULT_SCALE
                        view.scaleY = 1 - (position - 1) * DEFAULT_SCALE
                        view.translationY = ((position - 1) * view.measuredHeight / DEFAULT_TRANSLATE_Y).toFloat()
                    }
                    position > 0 -> {
                        view.scaleX = 1 - position * DEFAULT_SCALE
                        view.scaleY = 1 - position * DEFAULT_SCALE
                        view.translationY = (position * view.measuredHeight / DEFAULT_TRANSLATE_Y).toFloat()
                    }
                    else -> view.setOnTouchListener(mOnTouchListener)
                }
            }
        } else {
            for (position in itemCount - 1 downTo 0) {
                val view = recycler!!.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 5 + getDecoratedMeasuredHeight(view))

                if (position > 0) {
                    view.scaleX = 1 - position * DEFAULT_SCALE
                    view.scaleY = 1 - position * DEFAULT_SCALE
                    view.translationY = (position * view.measuredHeight / DEFAULT_TRANSLATE_Y).toFloat()
                } else {
                    view.setOnTouchListener(mOnTouchListener)
                }
            }
        }
    }
}