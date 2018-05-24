package com.thc.www.practice.support.behavior

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_behavior_translate.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/14
 */
class TranslateBehaviorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_behavior_translate)
        dependence_one.setOnClickListener {
            ViewCompat.offsetTopAndBottom(it, 5)
        }

        dependence_two.setOnClickListener {
            ViewCompat.offsetTopAndBottom(it, 5)
        }
    }
}