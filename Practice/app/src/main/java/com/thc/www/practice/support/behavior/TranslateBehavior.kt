package com.thc.www.practice.support.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/14
 */
// 注意一下，带有参数的这个构造必须要重载，因为在CoordinatorLayout里利用反射去获取这个Behavior的时候就是拿的这个构造
class TranslateBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<TextView>(context, attrs) {
    /**
     * 表示是否给应用了Behavior 的View 指定一个依赖的布局，通常，当依赖的View 布局发生变化时
     * 不管被被依赖View 的顺序怎样，被依赖的View也会重新布局
     * @param parent
     * @param child 绑定behavior 的View
     * @param dependency   依赖的view
     * @return 如果child 是依赖的指定的View 返回true,否则返回false
     */
    override fun layoutDependsOn(parent: CoordinatorLayout?,
                                 child: TextView?/*需要设置behavior的View*/,
                                 dependency: View?/*依赖的View*/): Boolean {
        // 如果我们关心一个TextView，layoutDependsOn可以这么写,
        return dependency is TextView
    }

    /**
     * 当被依赖的View 状态（如：位置、大小）发生变化时，这个方法被调用
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    override fun onDependentViewChanged(parent: CoordinatorLayout?,
                                        child: TextView?/*需要设置behavior的View*/,
                                        dependency: View?/*依赖的View*/): Boolean {
        // 设置好关心谁，接下来就是在这个View状态发生变化的时候，我们现在的View该做些什么了
        // 这要在onDependentViewChanged做工作了。这里获取dependency距离底部的距离，并且设置给child
        val offset = dependency!!.top - child!!.top
        ViewCompat.offsetTopAndBottom(child, offset)
        return true
    }


    /**
     *  当coordinatorLayout 的子View试图开始嵌套滑动的时候被调用。当返回值为true的时候表明
     *  coordinatorLayout 充当nested scroll parent 处理这次滑动，需要注意的是只有当返回值为true
     *  的时候，Behavior 才能收到后面的一些nested scroll 事件回调（如：onNestedPreScroll、onNestedScroll等）
     *  这个方法有个重要的参数nestedScrollAxes，表明处理的滑动的方向。
     *
     * @param coordinatorLayout 和Behavior 绑定的View的父CoordinatorLayout
     * @param child  和Behavior 绑定的View
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes 嵌套滑动 应用的滑动方向，看 {@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
     *                         {@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @return
     */
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: TextView,
                                     directTargetChild: View,
                                     target: View,
                                     axes: Int,
                                     type: Int): Boolean {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }


    /**
     * 嵌套滚动发生之前被调用
     * 在nested scroll child 消费掉自己的滚动距离之前，嵌套滚动每次被nested scroll child
     * 更新都会调用onNestedPreScroll。注意有个重要的参数consumed，可以修改这个数组表示你消费
     * 了多少距离。假设用户滑动了100px,child 做了90px 的位移，你需要把 consumed［1］的值改成90，
     * 这样coordinatorLayout就能知道只处理剩下的10px的滚动。
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx  用户水平方向的滚动距离
     * @param dy  用户竖直方向的滚动距离
     * @param consumed
     */
    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout,
                                   child: TextView,
                                   target: View,
                                   dx: Int,
                                   dy: Int,
                                   consumed: IntArray,
                                   type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    /**
     * 进行嵌套滚动时被调用
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed target 已经消费的x方向的距离
     * @param dyConsumed target 已经消费的y方向的距离
     * @param dxUnconsumed x 方向剩下的滚动距离
     * @param dyUnconsumed y 方向剩下的滚动距离
     */
    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: TextView,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int,
                                type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    /**
     *  嵌套滚动结束时被调用，这是一个清除滚动状态等的好时机。
     * @param coordinatorLayout
     * @param child
     * @param target
     */
    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout,
                                    child: TextView,
                                    target: View,
                                    type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

    /**
     * onStartNestedScroll返回true才会触发这个方法，接受滚动处理后回调，可以在这个
     * 方法里做一些准备工作，如一些状态的重置等。
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     */
    override fun onNestedScrollAccepted(coordinatorLayout: CoordinatorLayout,
                                        child: TextView,
                                        directTargetChild: View,
                                        target: View,
                                        axes: Int,
                                        type: Int) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    /**
     * 用户松开手指并且会发生惯性动作之前调用，参数提供了速度信息，可以根据这些速度信息
     * 决定最终状态，比如滚动Header，是让Header处于展开状态还是折叠状态。返回true 表
     * 示消费了fling.
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX x 方向的速度
     * @param velocityY y 方向的速度
     * @return
     */
    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: TextView, target: View, velocityX: Float, velocityY: Float): Boolean {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    //可以重写这个方法对子View 进行重新布局
    override fun onLayoutChild(parent: CoordinatorLayout?, child: TextView?, layoutDirection: Int): Boolean {
        return super.onLayoutChild(parent, child, layoutDirection)
    }
}