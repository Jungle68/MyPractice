package com.thc.www.practice.viewpager.rotation

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.thc.www.practice.R
import com.thc.www.practice.utils.DisplayUtil
import kotlinx.android.synthetic.main.activity_view_pager_gallery_3d.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/22
 */
class GalleryRotationActivity : AppCompatActivity() {
    companion object {
        private const val CENTER_PAGE_SCALE = 0.8f
    }

    private val imgs = arrayOf(R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4, R.mipmap.img5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager_gallery_rotation)

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
            setPageTransformer(true) { view, position ->
                val pagerWidth = this.width
                val horizontalOffsetBase = (pagerWidth - pagerWidth * CENTER_PAGE_SCALE) / 2 / offscreenPageLimit + DisplayUtil.dp2px(15F, context)

                if (position >= offscreenPageLimit || position <= -1) {
                    view.visibility = View.GONE
                } else {
                    view.visibility = View.VISIBLE
                }

                if (position >= 0) {
                    val translationX = (horizontalOffsetBase - view.width) * position
                    view.translationX = translationX
                }
                if (position > -1 && position < 0) {
                    val rotation = position * 30
                    view.rotation = rotation
                    view.alpha = (position * position * position + 1)
                } else if (position > offscreenPageLimit - 1) {
                    view.alpha = (1 - position + Math.floor(position.toDouble())).toFloat()
                } else {
                    view.rotation = 0F
                    view.alpha = 1F
                }
                if (position == 0F) {
                    view.scaleX = CENTER_PAGE_SCALE
                    view.scaleY = CENTER_PAGE_SCALE
                } else {
                    val scaleFactor = Math.min(CENTER_PAGE_SCALE - position * 0.1f, CENTER_PAGE_SCALE)
                    view.scaleX = scaleFactor
                    view.scaleY = scaleFactor
                }

                ViewCompat.setElevation(view, (offscreenPageLimit - position) * 5)
            }
        }

        //viewPager左右两边滑动无效的处理
        ll_gallery_outer.setOnTouchListener { _, event ->
            vp_gallery_vp.dispatchTouchEvent(event)
        }
    }
}