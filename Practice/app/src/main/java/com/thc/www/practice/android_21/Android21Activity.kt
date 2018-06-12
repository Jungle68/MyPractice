package com.thc.www.practice.android_21

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import com.thc.www.practice.android_21.share.FirstActivity
import com.thc.www.practice.android_21.transition.ConstraintSetActivity
import com.thc.www.practice.android_21.transition.PlaceHolderActivity
import kotlinx.android.synthetic.main.activity_andorid_21.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class Android21Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_andorid_21)

        btn_share.setOnClickListener {
            startActivity(Intent(this@Android21Activity, FirstActivity::class.java))
        }
        btn_transition_place_holder.setOnClickListener {
            startActivity(Intent(this@Android21Activity, PlaceHolderActivity::class.java))
        }
        btn_transition_constrain_set.setOnClickListener {
            startActivity(Intent(this@Android21Activity, ConstraintSetActivity::class.java))
        }
    }
}