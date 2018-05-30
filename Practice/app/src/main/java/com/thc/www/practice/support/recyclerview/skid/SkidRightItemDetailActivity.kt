package com.thc.www.practice.support.recyclerview.skid


import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_recycler_skid_item_detail.*

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */


class SkidRightItemDetailActivity : AppCompatActivity() {
    private var mImgPath: Int = 0
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_recycler_skid_item_detail)
        if (intent != null) {
            mImgPath = intent.getIntExtra("img", R.mipmap.skid_right_3)
            val title = intent.getStringExtra("title")
            tv_title.text = title
            Glide.with(this)
                    .load(mImgPath)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(img_bg)
            Handler().postDelayed({
                Glide.with(this@SkidRightItemDetailActivity)
                        .load(mImgPath)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(img_gif)
            },
                    1000)
        }
    }


    override fun onBackPressed() {
        img_gif.visibility = View.INVISIBLE
        super.onBackPressed()
    }
}
