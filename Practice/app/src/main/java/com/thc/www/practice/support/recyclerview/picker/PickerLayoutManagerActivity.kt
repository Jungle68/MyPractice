package com.thc.www.practice.support.recyclerview.picker

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_recycler_picker.*
import java.util.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class PickerLayoutManagerActivity : AppCompatActivity() {
    private var mPickerLayoutManager1: PickerLayoutManager? = null
    private var mPickerLayoutManager2: PickerLayoutManager? = null
    private val mHours = ArrayList<String>()
    private val mMinutes = ArrayList<String>()

    init {
        for (i in 0..24) {
            if (i <= 9) {
                mHours.add("0$i")
            } else {
                mHours.add(i.toString())
            }
        }

        for (i in 0..60) {
            if (i <= 9) {
                mMinutes.add("0$i")
            } else {
                mMinutes.add(i.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_recycler_picker)

        recycler1.run {
            layoutManager = PickerLayoutManager(this@PickerLayoutManagerActivity,
                    this,
                    LinearLayoutManager.VERTICAL,
                    false,
                    3,
                    0.4F,
                    true)
                    .apply {
                        onSelectedViewListener(object : PickerLayoutManager.OnSelectedViewListener {
                            override fun onSelectedView(view: View, position: Int) {
                                val textView = view as TextView?
                                if (textView != null)
                                    tv_hour.text = textView.text
                            }

                        })
                    }
            adapter = MyAdapter(mHours)
        }
        recycler2.run {
            layoutManager = PickerLayoutManager(this@PickerLayoutManagerActivity,
                    this,
                    LinearLayoutManager.VERTICAL,
                    false,
                    3,
                    0.4F,
                    true)
                    .apply {
                        onSelectedViewListener(object : PickerLayoutManager.OnSelectedViewListener {
                            override fun onSelectedView(view: View, position: Int) {
                                val textView = view as TextView?
                                if (textView != null)
                                    tv_minute.text = textView.text
                            }
                        })
                    }
            adapter = MyAdapter(mMinutes)
        }
    }

    internal inner class MyAdapter(private val mList: List<String>) : RecyclerView.Adapter<ViewHolder>() {
        private val mColors = intArrayOf(Color.YELLOW, Color.RED)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@PickerLayoutManagerActivity).inflate(R.layout.item_picker, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvText.text = mList[position]
        }

        override fun getItemCount(): Int {
            return mList.size
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvText: TextView = itemView.findViewById(R.id.tv_text)

    }
}