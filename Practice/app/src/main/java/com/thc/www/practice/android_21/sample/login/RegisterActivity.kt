package com.thc.www.practice.android_21.sample.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_android_21_sample_login_register.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_21_sample_login_register)
        showEnterAnimation()
        fab.setOnClickListener { animateRevealClose() }
    }

    private fun showEnterAnimation() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition)
        window.sharedElementEnterTransition = transition

        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                cv_add.visibility = View.GONE
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }


        })
    }

    fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(cv_add, cv_add.width / 2, 0, (fab.width / 2).toFloat(), cv_add.height.toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                cv_add.visibility = View.VISIBLE
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    private fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(cv_add, cv_add.width / 2, 0, cv_add.height.toFloat(), (fab.width / 2).toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                cv_add.visibility = View.INVISIBLE
                super.onAnimationEnd(animation)
                fab.setImageResource(R.mipmap.plus)
                super@RegisterActivity.onBackPressed()
            }

        })
        mAnimator.start()
    }

    override fun onBackPressed() {
        animateRevealClose()
    }
}