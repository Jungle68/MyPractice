package com.thc.www.practice.support.behavior

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_behavior.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/14
 */
class BehaviorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_behavior)

        btn_translate.setOnClickListener {
            startActivity(Intent(this@BehaviorActivity, TranslateBehaviorActivity::class.java))
        }

        btn_scroll.setOnClickListener {
            startActivity(Intent(this@BehaviorActivity, ScrollBehaviorActivity::class.java))
        }

        btn_zhihu.setOnClickListener {
            startActivity(Intent(this@BehaviorActivity, ZhihuBehaviorActivity::class.java))
        }

        btn_cover_header.setOnClickListener {
            startActivity(Intent(this@BehaviorActivity, CoverHeaderScrollBehaviorActivity::class.java))
        }
    }
}