package com.thc.www.practice.support.recyclerview.skid

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.thc.www.practice.R
import com.thc.www.practice.utils.DisplayUtil
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class SkidRightLayoutManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_recycler_view)

        recyclerView.run {
            (layoutParams as FrameLayout.LayoutParams).setMargins(0, 150, 0, 150)
            layoutManager = SkidRightLayoutManager(1.5F, 0.85F)
            adapter = MyAdapter()
        }
    }

    /**
     * 适配器
     */
    internal inner class MyAdapter : RecyclerView.Adapter<ViewHolder>() {
        private val imgs = intArrayOf(R.mipmap.skid_right_1, R.mipmap.skid_right_2, R.mipmap.skid_right_3, R.mipmap.skid_right_4, R.mipmap.skid_right_5, R.mipmap.skid_right_6, R.mipmap.skid_right_7)
        private var titles = arrayOf("Acknowl", "Belief", "Confidence", "Dreaming", "Happiness", "Confidence")

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@SkidRightLayoutManagerActivity).inflate(R.layout.item_skid_right, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(this@SkidRightLayoutManagerActivity)
                    .load(imgs[position % 7])
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(holder.imgBg)
            holder.tvTitle.text = titles[position % 6]
            holder.imgBg.setOnClickListener {
                val intent = Intent(this@SkidRightLayoutManagerActivity, SkidRightItemDetailActivity::class.java)
                intent.putExtra("img", imgs[position % 7])
                intent.putExtra("title", titles[position % 6])
                val p1 = Pair.create(holder.imgBg as View, "img_view_1")
                val p2 = Pair.create(holder.tvTitle as View, "title_1")
                val p3 = Pair.create(holder.tvBottom as View, "tv_bottom")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SkidRightLayoutManagerActivity, p1, p2, p3)
                startActivity(intent, options.toBundle())
            }
        }

        override fun getItemCount(): Int {
            return 20
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var imgBg: ImageView = itemView.findViewById(R.id.img_bg)
        internal var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        internal var tvBottom: TextView = itemView.findViewById(R.id.tv_bottom)
    }
}