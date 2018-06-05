package com.thc.www.practice.support.recyclerview.banner


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_recycler_banner.*
import java.util.*


class BannerLayoutManagerActivity : AppCompatActivity() {
    private val mImgList = ArrayList<ImageView>()
    private var mLastSelectPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_recycler_banner)

        initView()
    }

    private fun initView() {
        mImgList.add(img_1)
        mImgList.add(img_2)
        mImgList.add(img_3)
        mImgList.add(img_4)

        /*广告轮播图*/

        recycler1.run {
            layoutManager = BannerLayoutManager(this@BannerLayoutManagerActivity, recycler1, 4, OrientationHelper.HORIZONTAL)
                    .apply {
                        setOnSelectedViewListener(object : BannerLayoutManager.OnSelectedViewListener {
                            override fun onSelectedView(view: View, position: Int) {
                                changeUI(position)
                            }
                        })
                    }
            adapter = MyAdapter()
        }
        changeUI(0)

        /*消息轮播*/
        recycler2.run {
            layoutManager = BannerLayoutManager(this@BannerLayoutManagerActivity, recycler2, 4, OrientationHelper.VERTICAL).apply {
                setTimeSmooth(400f)
            }
            adapter = MyNewsAdapter()
        }
    }

    private fun changeUI(position: Int) {
        if (position != mLastSelectPosition) {
            mImgList[position].setImageDrawable(resources.getDrawable(R.drawable.circle_red))
            mImgList[mLastSelectPosition].setImageDrawable(resources.getDrawable(R.drawable.circle_ring_gray))
            mLastSelectPosition = position
        }
    }

    /**
     * 图片轮播适配器
     */
    internal inner class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        private val imgs = intArrayOf(R.mipmap.banner_1, R.mipmap.banner_2, R.mipmap.banner_3, R.mipmap.banner_4)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@BannerLayoutManagerActivity).inflate(R.layout.item_banner, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.img.setImageResource(imgs[position % 4])
        }

        override fun getItemCount(): Int {
            return Integer.MAX_VALUE
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var img: ImageView = itemView.findViewById(R.id.img)
        }
    }

    /**
     * 新闻轮播适配器
     */
    internal inner class MyNewsAdapter : RecyclerView.Adapter<MyNewsAdapter.ViewHolder>() {
        private val mTitles = arrayOf("小米8官方宣布有双路GPS,小米8、小米8SE发售时间曝光", "这样的锤子你玩懂了吗?坚果R1带来不一样的体验", "三星真的很爱酸苹果!新广告讽刺苹果手机电池降速事件", "双摄全面屏 游戏长续航 魅族科技发布魅蓝6T售799元起")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@BannerLayoutManagerActivity).inflate(R.layout.item_banner_news, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tv_news.text = mTitles[position % 4]
        }

        override fun getItemCount(): Int {
            return Integer.MAX_VALUE
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var tv_news: TextView = itemView.findViewById(R.id.tv_news)
        }
    }
}
