package com.thc.www.practice

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.android_21.Android21Activity
import com.thc.www.practice.sample.SampleActivity
import com.thc.www.practice.support.SupportActivity
import com.thc.www.practice.thridpart.ThirdPartActivity
import com.thc.www.practice.viewpager.ViewPagerActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_android_21.setOnClickListener {
            startActivity(Intent(this@MainActivity, Android21Activity::class.java))
        }

        btn_support.setOnClickListener {
            startActivity(Intent(this@MainActivity, SupportActivity::class.java))
        }

        btn_view_pager.setOnClickListener {
            startActivity(Intent(this@MainActivity, ViewPagerActivity::class.java))
        }

        btn_sample.setOnClickListener {
            startActivity(Intent(this@MainActivity, SampleActivity::class.java))
        }
        btn_third_part.setOnClickListener {
            startActivity(Intent(this@MainActivity, ThirdPartActivity::class.java))
        }
    }
}