package com.thc.www.practice.support.recyclerview.linearlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class CustomerLayoutManagerActivity : AppCompatActivity() {
    private val datas = ArrayList<String>().apply {
        for (i in 0..50) {
            add("第" + i + "个")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_recycler_view)

        recyclerView.run {
            layoutManager = CustomerLayoutManager()
            adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                        ViewHolder(layoutInflater.inflate(R.layout.android_21_share_item, parent, false))

                override fun getItemCount(): Int = datas.size

                override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)
            }
        }
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val textView: TextView = itemView.findViewById(R.id.tv_content)
        fun bindView(position: Int) {
            textView.text = datas[position]
        }
    }
}