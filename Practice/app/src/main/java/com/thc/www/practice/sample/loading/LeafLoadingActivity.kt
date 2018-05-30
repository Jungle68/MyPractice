package com.thc.www.practice.sample.loading

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import com.thc.www.practice.R
import com.thc.www.practice.utils.AnimationUtils
import kotlinx.android.synthetic.main.activity_sample_leaf_loading.*
import java.util.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class LeafLoadingActivity : AppCompatActivity() {
    private var REFRESH_PROGRESS = 0x10
    private var mProgress = 0

    private var mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                REFRESH_PROGRESS -> if (mProgress < 40) {
                    mProgress += 1
                    // 随机800ms以内刷新一次
                    this.sendEmptyMessageDelayed(REFRESH_PROGRESS, Random().nextInt(800).toLong())
                    leaf_loading.setProgress(mProgress)
                } else {
                    mProgress += 1
                    // 随机1200ms以内刷新一次
                    this.sendEmptyMessageDelayed(REFRESH_PROGRESS, Random().nextInt(1200).toLong())
                    leaf_loading.setProgress(mProgress)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_leaf_loading)
        initViews()
        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000)
    }

    private fun initViews() {
        val rotateAnimation = AnimationUtils.initRotateAnimation(false, 1500, true,
                Animation.INFINITE)
        fan_pic.startAnimation(rotateAnimation)
    }
}