package com.thc.www.practice.support.sliding_pane_layout

import android.os.Bundle
import android.support.v4.widget.SlidingPaneLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_sliding_pane.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class SlidingPaneLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_sliding_pane)
        slidingPaneLayout.setPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                //第一种滑动效果 slideOffset最大1.0F
                fl_left.scaleY = slideOffset / 5 + 0.8F
                fl_left.scaleX = slideOffset / 5 + 0.8F
                fl_right.scaleY = 1 - slideOffset / 5
                fl_right.scaleX = 1 - slideOffset / 5

                //第二种滑动效果把上面3行注释就OK，更多好看的滑动效果实现就要靠你自己摸索了。
            }

            override fun onPanelClosed(panel: View) {
            }

            override fun onPanelOpened(panel: View) {
            }
        })
    }
}
