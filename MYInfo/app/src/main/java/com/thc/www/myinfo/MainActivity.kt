package com.thc.www.myinfo

import android.os.Bundle
import com.nightonke.wowoviewpager.Animation.WoWoScaleAnimation
import com.nightonke.wowoviewpager.Animation.WoWoShapeColorAnimation
import com.nightonke.wowoviewpager.Enum.Ease
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MyBaseActivity() {
    private var r: Int = 0
    override fun contentViewRes(): Int = R.layout.activity_main

    override fun fragmentNumber(): Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        r = Math.sqrt((screenW * screenW + screenH * screenH) * 1.0).toInt() + 10
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        addAnimations()
    }

    private fun addAnimations() {
        if (animationAdded) return
        animationAdded = true

        addCircleAnimation()
        wowo.ready()
    }

    private fun addCircleAnimation() {
        wowo.addAnimation(circle)
                .add(WoWoShapeColorAnimation.builder().page(0)
                        .from(color(R.color.gray)).to(color(R.color.light_blue)).build())
                .add(WoWoScaleAnimation.builder().page(0)
                        .fromXY(1f).toX((r * 2 / circle.width).toFloat()).toY((r * 2 / circle.height).toFloat())
                        .ease(Ease.InBack).sameEaseBack(false).build())
    }

    override fun fragmentColorsRes(): Array<Int> {
        return arrayOf(R.color.black_background, R.color.black_background, R.color.black_background, R.color.black_background)
    }
}
