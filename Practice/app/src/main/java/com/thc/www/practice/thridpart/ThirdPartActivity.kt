package com.thc.www.practice.thridpart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_third_part.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/6/11
 */
class ThirdPartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_part)

        btn_smart_refresh_layout.setOnClickListener {
            startActivity(Intent(this@ThirdPartActivity, SmartRefreshLayoutActivity::class.java))
        }
    }
}