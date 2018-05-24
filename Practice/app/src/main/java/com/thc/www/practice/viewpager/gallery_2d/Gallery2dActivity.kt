package com.thc.www.practice.viewpager.gallery_2d

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_view_pager_gallery_2d.*


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/22
 */
class Gallery2dActivity : AppCompatActivity() {
    companion object {
        //自由控制缩放比例
        private const val MAX_SCALE = 1f
        private const val MIN_SCALE = 0.85f//0.85f
    }

    private val imgs = arrayOf(R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4, R.mipmap.img5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager_gallery_2d)

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

            offscreenPageLimit = 2  //设置预加载数量
            pageMargin = 0  //控制两幅图之间的间距，也可以通过transformPage中去控制
            setPageTransformer(true) { page, position ->
                if (position <= 1 && position >= -1) {
                    val scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE)
                    page.scaleX = scaleFactor
                    page.scaleY = scaleFactor
                    if (position > 0) {
                        page.translationX = -scaleFactor * 2
                    } else if (position < 0) {
                        page.translationX = scaleFactor * 2
                    }
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