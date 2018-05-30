package com.thc.www.practice.support.recyclerview.vertical_scroll

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
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
class EcheIonLayoutManagerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_recycler_view)

        recyclerView.run {
            layoutManager = EchelonLayoutManager()
            adapter =MyAdapter()
        }
    }

    inner class MyAdapter : RecyclerView.Adapter<ViewHolder>() {
        private val bgs = intArrayOf(R.mipmap.bg_1, R.mipmap.bg_2, R.mipmap.bg_3, R.mipmap.bg_4)
        private val nickNames = arrayOf("左耳近心", "凉雨初夏", "稚久九栀", "半窗疏影")
        private val descs = arrayOf("回不去的地方叫故乡 没有根的迁徙叫流浪...", "人生就像迷宫，我们用上半生找寻入口，用下半生找寻出口", "原来地久天长，只是误会一场", "不是故事的结局不够好，而是我们对故事的要求过多", "只想优雅转身，不料华丽撞墙")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_echelon, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.nickName.text = nickNames[position % 4]
            holder.desc.text = descs[position % 5]
            holder.bg.setImageResource(bgs[position % 4])
        }

        override fun getItemCount(): Int {
            return 60
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bg: ImageView = itemView.findViewById(R.id.img_bg)
        var nickName: TextView = itemView.findViewById(R.id.tv_nickname)
        var desc: TextView = itemView.findViewById(R.id.tv_desc)
    }
}