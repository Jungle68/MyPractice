package com.thc.www.practice.viewpager.gallery_3d

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_view_pager_gallery_3d.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/22
 */
class Gallery3dActivity : AppCompatActivity() {
    companion object {
        private const val MIN_SCALE = 0.85f
    }

    private val imgs = arrayOf(R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4, R.mipmap.img5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager_gallery_3d)

        vp_gallery_vp.apply {
            adapter = object : PagerAdapter() {
                override fun isViewFromObject(view: View, any: Any): Boolean = view == any

                override fun getCount(): Int = imgs.size

                override fun instantiateItem(container: ViewGroup, position: Int): Any {
                    val view = layoutInflater.inflate(R.layout.single_image_view, container, false) as ImageView
                    view.setImageResource(imgs[position])
                    container.addView(view)
                    return view
                }

                override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
                    container.removeView(any as View)
                }
            }

            offscreenPageLimit = imgs.size  //设置预加载数量
            pageMargin = 10  //控制两幅图之间的间距
            setPageTransformer(true) { page, position ->
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val rotate = -10 * position
                //position小于等于1的时候，代表page已经位于中心item的最左边，
                //此时设置为最小的缩放率以及最大的旋转度数
                page.rotationY = rotate
                if (position <= 1 && position >= -1) {
                    page.scaleX = scaleFactor
                    page.scaleY = scaleFactor
                } else {
                    page.scaleX = MIN_SCALE
                    page.scaleY = MIN_SCALE
                }
            }
        }

        //viewPager左右两边滑动无效的处理
        ll_gallery_outer.setOnTouchListener { _, event ->
            vp_gallery_vp.dispatchTouchEvent(event)
        }
    }
}