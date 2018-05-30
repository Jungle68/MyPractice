package com.thc.www.practice.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import com.thc.www.practice.sample.loading.LeafLoadingActivity
import com.thc.www.practice.sample.login.LoginActivity
import kotlinx.android.synthetic.main.activity_sample.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        btn_login_page.setOnClickListener {
            startActivity(Intent(this@SampleActivity, LoginActivity::class.java))
        }

        btn_leaf_loading.setOnClickListener {
            startActivity(Intent(this@SampleActivity, LeafLoadingActivity::class.java))
        }
    }
}