package com.thc.www.practice.support

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import com.thc.www.practice.support.behavior.BehaviorActivity
import com.thc.www.practice.support.bottom_sheet_dialog.BottomSheetActivity
import com.thc.www.practice.support.recyclerview.RecyclerViewActivity
import com.thc.www.practice.support.sliding_pane_layout.SlidingPaneLayoutActivity
import kotlinx.android.synthetic.main.activity_support.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class SupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        btn_sliding.setOnClickListener {
            startActivity(Intent(this@SupportActivity, SlidingPaneLayoutActivity::class.java))
        }

        btn_behavior.setOnClickListener {
            startActivity(Intent(this@SupportActivity, BehaviorActivity::class.java))
        }

        btn_bottom_sheet.setOnClickListener {
            startActivity(Intent(this@SupportActivity, BottomSheetActivity::class.java))
        }

        btn_recycler_view.setOnClickListener {
            startActivity(Intent(this@SupportActivity, RecyclerViewActivity::class.java))
        }
    }
}