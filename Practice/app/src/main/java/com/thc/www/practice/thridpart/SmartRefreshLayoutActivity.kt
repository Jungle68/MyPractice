package com.thc.www.practice.thridpart

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_third_part_refresh_layout.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/6/11
 */
class SmartRefreshLayoutActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_part_refresh_layout)
        srl_main.isEnableRefresh = false
    }
}