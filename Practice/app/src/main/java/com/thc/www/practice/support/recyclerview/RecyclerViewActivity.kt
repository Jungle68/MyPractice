package com.thc.www.practice.support.recyclerview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_recylcer_view.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class RecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_recylcer_view)

        btn_customer_layout_manager_linear.setOnClickListener {
            startActivity(Intent(this@RecyclerViewActivity, CustomerLayoutManagerActivity::class.java))
        }
    }
}