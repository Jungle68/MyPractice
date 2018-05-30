package com.thc.www.practice.support.recyclerview.linearlayout

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class CustomerLayoutManager : RecyclerView.LayoutManager() {
    private var verticalScrollOffset: Int = 0
    private var totalHeight: Int = 0
    //保存所有的Item的上下左右的偏移量信息
    private val allItemFrames = SparseArray<Rect>()

    /**
     * 这个方法就是RecyclerView Item的布局参数，换种说法，就是RecyclerView 子 item 的 LayoutParameters，
     * 若是想修改子Item的布局参数（比如：宽/高/margin/padding等等），那么可以在该方法内进行设置。
     * 一般来说，没什么特殊需求的话，则可以直接让子item自己决定自己的宽高即可（wrap_content）。
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    /**
     * 这个方法就类似于自定义ViewGroup的onLayout方法，这也是自定义LayoutOutManager的主要入口（重要）
     * You must override onLayoutChildren(Recycler recycler, State state)
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        //如果没有item，直接返回
        if (itemCount <= 0) return

        // 跳过preLayout，preLayout主要用于支持动画
        if (state!!.isPreLayout) {
            return
        }
        // 在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler)
        //定义竖直方向的偏移量
        var offsetY = 0
        totalHeight = 0
        allItemFrames.clear()
        for (i in 0 until itemCount) {
            //这里就是从缓存里面取出
            val view = recycler?.getViewForPosition(i)
            //将View加入到RecyclerView中
            addView(view)
            measureChildWithMargins(view, 0, 0)
            val width = getDecoratedMeasuredWidth(view)
            val height = getDecoratedMeasuredHeight(view)

            totalHeight += height
            var frame = allItemFrames.get(i)
            if (frame == null) {
                frame = Rect()
            }
            frame.set(0, offsetY, width, offsetY + height)
            // 将当前的Item的Rect边界数据保存
            allItemFrames.put(i, frame)

            //将竖直方向偏移量增大height
            offsetY += height
        }
        // 如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        totalHeight = Math.max(totalHeight, getVerticalSpace())

        recycleAndFillItems(recycler, state)
    }

    /**
     * 回收不需要的Item，并且将需要显示的Item从缓存中取出
     */
    private fun recycleAndFillItems(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (state!!.isPreLayout) { // 跳过preLayout，preLayout主要用于支持动画
            return
        }

        // 当前scroll offset状态下的显示区域
        val displayFrame = Rect(0, verticalScrollOffset, getHorizontalSpace(), verticalScrollOffset + getVerticalSpace())

        /**
         * 将滑出屏幕的Items回收到Recycle缓存中
         */
        for (i in 0 until itemCount) {
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayFrame, allItemFrames[i])) {
                //回收掉滑出屏幕的View
                val child = getChildAt(i)
                if (child != null)
                    removeAndRecycleView(child, recycler)
            }
        }

        //重新显示需要出现在屏幕的子View
        for (i in 0 until itemCount) {
            if (Rect.intersects(displayFrame, allItemFrames.get(i))) {
                val scrap = recycler!!.getViewForPosition(i)
                measureChildWithMargins(scrap, 0, 0)
                addView(scrap)

                val frame = allItemFrames.get(i)
                //将这个item布局出来
                layoutDecorated(scrap,
                        frame.left,
                        frame.top - verticalScrollOffset,
                        frame.right,
                        frame.bottom - verticalScrollOffset)
            }
        }
    }

    /**
     * 那么如何设置滚动呢？
     * 首先，重写canScrollVertically()函数，并返回true。
     * 同理，如果实现水平方向的滑动，则重写canScrollHorizontally()并返回true
     */
    override fun canScrollVertically(): Boolean {
        return true
    }

    /**
     * 然后重写scrollVerticallyBy()函数，用于实现垂直方向滑动，
     * 同理，如果你想要实现水平方向的滑动那么重写scrollHorizontallyBy()函数。
     */
    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        //先detach掉所有的子View
        detachAndScrapAttachedViews(recycler)

        //实际要滑动的距离
        var travel = dy

        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {//如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset
        }

        //将竖直方向的偏移量+travel
        verticalScrollOffset += travel

        // 平移容器内的item
        offsetChildrenVertical(-travel)
        recycleAndFillItems(recycler, state)
        Log.e("Test", "itemCount count:$itemCount")
        Log.e("Test", "childCount count:$childCount")

        return travel
    }

    private fun getVerticalSpace(): Int {
        return height - paddingBottom - paddingTop
    }

    private fun getHorizontalSpace(): Int {
        return width - paddingRight - paddingLeft
    }
}