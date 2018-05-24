package com.thc.www.practice.android_21.share

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_android_21_share_first.*

class FirstActivity : AppCompatActivity() {
    val datas: ArrayList<String> = ArrayList()

    init {
        datas.apply {
            add("#ffFF33ff")
            add("#0ff0ff")
            add("#005084")
            add("#fcb840")
            add("#4f70bb")
            add("#ffaa66cc")
            add("#dc4b3a")
            add("#F3F3F3")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_21_share_first)
        rv_container.run {
            layoutManager = LinearLayoutManager(this@FirstActivity, LinearLayoutManager.VERTICAL, false)
            adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                    holder.bindView(position)
                }

                override fun getItemCount(): Int = datas.size


                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                        ViewHolder(layoutInflater.inflate(R.layout.android_21_share_item, parent, false))
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)

        fun bindView(position: Int) {
            tvContent.text = datas[position]
            ivIcon.setBackgroundColor(Color.parseColor(datas[position]))
            itemView.setOnClickListener {
                val intent = Intent(this@FirstActivity, SecondActivity::class.java)

                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@FirstActivity, ivIcon, "share").toBundle())
            }
        }
    }
}
