package com.thc.www.practice.support.recyclerview.vertical_scroll

import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/30
 */
class EchelonLayoutManager : RecyclerView.LayoutManager() {
    private var mItemViewWidth: Int = 0
    private var mItemViewHeight: Int = 0
    private var mItemCount: Int = 0
    private var mScrollOffset = Integer.MAX_VALUE
    private val mScale = 0.9f

    override fun generateDefaultLayoutParams() =
            RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0 || state.isPreLayout) return
        removeAndRecycleAllViews(recycler)

        mItemViewWidth = (getHorizontalSpace() * 0.87f).toInt()
        mItemViewHeight = (mItemViewWidth * 1.46f).toInt()
        mItemCount = itemCount
        //获取到所有Item的高度和
        mScrollOffset = Math.min(Math.max(mItemViewHeight, mScrollOffset), mItemCount * mItemViewHeight)

        layoutChild(recycler)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val pendingScrollOffset = mScrollOffset + dy
        mScrollOffset = Math.min(Math.max(mItemViewHeight, mScrollOffset + dy), mItemCount * mItemViewHeight)
        layoutChild(recycler)
        return mScrollOffset - pendingScrollOffset + dy
    }


    override fun canScrollVertically() = true

    private fun layoutChild(recycler: RecyclerView.Recycler) {
        if (itemCount == 0) return
        //获取到最后一个Item的位置
        var bottomItemPosition = Math.floor(mScrollOffset / mItemViewHeight * 1.0).toInt()
        //获取到出去一个完整的Item的高度，还剩余多少空间
        var remainSpace = getVerticalSpace() - mItemViewHeight
        //滑动的时候可以获取到最后一个Item在屏幕上还显示的高度
        val bottomItemVisibleHeight = mScrollOffset % mItemViewHeight
        //最后一个Item显示高度相对于本身的比例
        val offsetPercentRelativeToItemView = bottomItemVisibleHeight * 1.0f / mItemViewHeight
        //把我们需要的Item添加到这个集合
        val layoutInfos = ArrayList<ItemViewInfo>()
        run {
            var i = bottomItemPosition - 1
            var j = 1
            while (i >= 0) {
                //计算偏移量
                val maxOffset = (getVerticalSpace() - mItemViewHeight) / 2 * Math.pow(0.8, j.toDouble())
                //这个Item的top值
                val start = (remainSpace - offsetPercentRelativeToItemView * maxOffset).toInt()
                //这个Item需要缩放的比例
                val scaleXY = (Math.pow(mScale * 1.0, (j - 1).toDouble()) * (1 - offsetPercentRelativeToItemView * (1 - mScale))).toFloat()
                val positonOffset = offsetPercentRelativeToItemView
                //Item上面的距离占RecyclerView可用高度的比例
                val layoutPercent = start * 1.0f / getVerticalSpace()
                val info = ItemViewInfo(start, scaleXY, positonOffset, layoutPercent)
                layoutInfos.add(0, info)
                remainSpace = (remainSpace - maxOffset).toInt()
                //在添加Item的同时，计算剩余空间是否可以容下下一个Item，如果不能的话，就不再添加了
                if (remainSpace <= 0) {
                    info.top = (remainSpace + maxOffset).toInt()
                    info.positionOffset = 0F
                    info.layoutPercent = info.top / getVerticalSpace() * 1.0F
                    info.scaleXY = Math.pow(mScale * .10, (j - 1).toDouble()).toFloat()
                    break
                }
                i--
                j++
            }
        }

        if (bottomItemPosition < mItemCount) {
            val start = getVerticalSpace() - bottomItemVisibleHeight
            layoutInfos.add(ItemViewInfo(start, 1.0f, bottomItemVisibleHeight * 1.0f / mItemViewHeight, start * 1.0f / getVerticalSpace())
                    .setIsBottom())
        } else {
            bottomItemPosition -= 1//99
        }
        //这里做的是回收处理
        val layoutCount = layoutInfos.size
        val startPos = bottomItemPosition - (layoutCount - 1)
        val endPos = bottomItemPosition
        val childCount = childCount
        (childCount - 1 downTo 0).forEach { k ->
            val childView = getChildAt(k)
            val pos = getPosition(childView)
            if (pos > endPos || pos < startPos) {
                removeAndRecycleView(childView, recycler)
            }
        }

        detachAndScrapAttachedViews(recycler)
        //这里主要是对需要显示的Item进行排列以及缩放
        (0 until layoutCount).forEach { k ->
            val view = recycler.getViewForPosition(startPos + k)
            val layoutInfo = layoutInfos[k]
            addView(view)
            measureChildWithExactlySize(view)
            val left = (getHorizontalSpace() - mItemViewWidth) / 2
            layoutDecoratedWithMargins(view, left, layoutInfo.top, left + mItemViewWidth, layoutInfo.top + mItemViewHeight)
            view.pivotX = view.width / 2 * 1.0F
            view.pivotY = 0F
            view.scaleX = layoutInfo.scaleXY
            view.scaleY = layoutInfo.scaleXY
        }
    }

    /**
     * 测量itemview的确切大小
     */
    private fun measureChildWithExactlySize(child: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(mItemViewWidth, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(mItemViewHeight, View.MeasureSpec.EXACTLY)
        child.measure(widthSpec, heightSpec)
    }

    /**
     * 获取RecyclerView的显示高度
     */
    private fun getVerticalSpace(): Int {
        return height - paddingTop - paddingBottom
    }

    /**
     * 获取RecyclerView的显示宽度
     */
    private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }
}