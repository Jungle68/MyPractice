package com.thc.www.practice.sample.douyin

/**
 * @Describe
 * @Author thc
 * @Date  2018/6/5
 */
interface OnViewPagerListener {
    /*释放的监听*/
    fun onPageRelease(isNext: Boolean, position: Int)

    /*选中的监听以及判断是否滑动到底部*/
    fun onPageSelected(position: Int, isBottom: Boolean)

    /*布局完成的监听*/
    fun onLayoutComplete()
}