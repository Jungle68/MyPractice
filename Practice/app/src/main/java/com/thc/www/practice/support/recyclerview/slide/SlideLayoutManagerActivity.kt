package com.thc.www.practice.support.recyclerview.slide

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class SlideLayoutManagerActivity : AppCompatActivity() {
    private val mList = java.util.ArrayList<SlideBean>()
    private var mLikeCount = 50
    private var mDislikeCount = 50


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_recycler_view)
        addData()
        recyclerView.run {
            adapter = MyAdapter()
            layoutManager = SlideLayoutManger(this,
                    ItemTouchHelper(ItemTouchHelperCallback(adapter, mList).apply {
                        setOnSlideListener(object:OnSlideListener<SlideBean> {
                            override fun onSliding(viewHolder: RecyclerView.ViewHolder?, ratio: Float, direction: Int) {
                            }
                            override fun onSlided(viewHolder: RecyclerView.ViewHolder?, t: SlideBean?, direction: Int) {
                            }

                            override fun onClear() {
                                addData()
                            }
                        })
                    }))
        }
    }

    /**
     * 向集合中添加数据
     */
    private fun addData() {
        val titles = arrayOf("Acknowledging", "Belief", "Confidence", "Dreaming", "Happiness", "Confidence")
        val says = arrayOf("Do one thing at a time, and do well.", "Keep on going never give up.", "Whatever is worth doing is worth doing well.", "I can because i think i can.", "Jack of all trades and master of none.", "Keep on going never give up.", "Whatever is worth doing is worth doing well.")
        val bgs = intArrayOf(R.mipmap.img_slide_1, R.mipmap.img_slide_2, R.mipmap.img_slide_3, R.mipmap.img_slide_4, R.mipmap.img_slide_5, R.mipmap.img_slide_6)

        for (i in 0..5) {
            mList.add(SlideBean(bgs[i], titles[i], says[i]))
        }
    }

    /**
     * 适配器
     */
    inner class MyAdapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@SlideLayoutManagerActivity).inflate(R.layout.item_slide, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val bean = mList[position]
            holder.imgBg.setImageResource(bean.itemBg)
            holder.tvTitle.text = bean.title
            holder.userSay.text = bean.userSay
        }

        override fun getItemCount(): Int {
            return mList.size
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var imgBg: ImageView = itemView.findViewById(R.id.img_bg)
        internal var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        internal var userSay: TextView = itemView.findViewById(R.id.tv_user_say)
    }
}