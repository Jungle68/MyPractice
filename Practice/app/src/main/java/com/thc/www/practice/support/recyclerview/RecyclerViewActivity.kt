package com.thc.www.practice.support.recyclerview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import com.thc.www.practice.support.recyclerview.linearlayout.CustomerLayoutManagerActivity
import com.thc.www.practice.support.recyclerview.picker.PickerLayoutManagerActivity
import com.thc.www.practice.support.recyclerview.skid.SkidRightLayoutManagerActivity
import com.thc.www.practice.support.recyclerview.slide.SlideLayoutManagerActivity
import com.thc.www.practice.support.recyclerview.vertical_scroll.EcheIonLayoutManagerActivity
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
        btn_echeion_layout_manager.setOnClickListener {
            startActivity(Intent(this@RecyclerViewActivity, EcheIonLayoutManagerActivity::class.java))
        }
        btn_picker_layout_manager.setOnClickListener {
            startActivity(Intent(this@RecyclerViewActivity, PickerLayoutManagerActivity::class.java))
        }
        btn_slide_layout_manager.setOnClickListener {
            startActivity(Intent(this@RecyclerViewActivity, SlideLayoutManagerActivity::class.java))
        }
        btn_skid_layout_manager.setOnClickListener {
            startActivity(Intent(this@RecyclerViewActivity, SkidRightLayoutManagerActivity::class.java))
        }
    }
}