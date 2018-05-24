package com.thc.www.practice.android_21.share

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_android_21_share_second.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_21_share_second)
        img_green.setOnTouchListener({ _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val finalRadius = Math.hypot(ll_container.width.toDouble(), ll_container.height.toDouble()).toFloat()
                val anim = ViewAnimationUtils.createCircularReveal(tv_reveal2, event.rawX.toInt(), event.rawY.toInt(), 0F, finalRadius)
                anim.duration = 1000L
                anim.interpolator = AccelerateDecelerateInterpolator()
                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        tv_reveal2.visibility = View.VISIBLE
                    }

                })
                anim.start()
            }
            false
        })
    }
}