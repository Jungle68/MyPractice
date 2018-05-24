package com.thc.www.practice.viewpager

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.practice.R
import com.thc.www.practice.viewpager.gallery_2d.Gallery2dActivity
import com.thc.www.practice.viewpager.gallery_3d.Gallery3dActivity
import com.thc.www.practice.viewpager.rotation.GalleryRotationActivity
import kotlinx.android.synthetic.main.activity_view_pager.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/22
 */
class ViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        btn_gallery_2d.setOnClickListener {
            startActivity(Intent(this@ViewPagerActivity, Gallery2dActivity::class.java))
        }

        btn_gallery_3d.setOnClickListener {
            startActivity(Intent(this@ViewPagerActivity, Gallery3dActivity::class.java))
        }
        btn_gallery_rotation.setOnClickListener {
            startActivity(Intent(this@ViewPagerActivity, GalleryRotationActivity::class.java))
        }
    }
}