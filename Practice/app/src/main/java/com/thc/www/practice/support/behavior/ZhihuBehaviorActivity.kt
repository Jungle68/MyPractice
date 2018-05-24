package com.thc.www.practice.support.behavior

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thc.www.practice.R
import com.thc.www.practice.support.behavior.ZhihuFABBehavior.OnStateChangedListener
import kotlinx.android.synthetic.main.activity_support_behavior_zhihu.*


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/14
 */
class ZhihuBehaviorActivity : AppCompatActivity() {
    private var mBottomSheetBehavior: BottomSheetBehavior<View>? = null
    private val datas = ArrayList<String>().apply {
        add("1")
        add("2")
        add("3")
        add("4")
        add("5")
        add("6")
        add("7")
        add("8")
        add("9")
        add("10")
        add("11")
        add("12")
        add("13")
        add("14")
        add("15")
        add("16")
        add("17")
        add("18")
        add("19")
        add("20")
        add("21")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_behavior_zhihu)

        val scaleDownShowFab = ZhihuFABBehavior.from(fab)
        scaleDownShowFab.setOnStateChangedListener(onStateChangedListener)
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.tab_layout))
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        mBottomSheetBehavior?.peekHeight = 0

        recyclerView.run {
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(this@ZhihuBehaviorActivity, LinearLayoutManager.VERTICAL, false)
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

    private val onStateChangedListener = object : OnStateChangedListener {
        override fun onChanged(isShow: Boolean) {
            mBottomSheetBehavior?.state =
                    if (isShow) BottomSheetBehavior.STATE_EXPANDED
                    else BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}