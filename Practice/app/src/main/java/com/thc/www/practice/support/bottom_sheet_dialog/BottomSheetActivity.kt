package com.thc.www.practice.support.bottom_sheet_dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_support_bottom_sheet.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/15
 */
class BottomSheetActivity : AppCompatActivity() {
    private var mBottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_bottom_sheet)

        mBottomSheetBehavior = BottomSheetBehavior.from(tab_layout)
        mBottomSheetBehavior?.peekHeight = 0  // 如果设置BottomSheetBehavior.STATE_COLLAPSED这个属性没有效果，需设置peekHeight=0

        btn_bottom_sheet_control.setOnClickListener {
            if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            } else if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
                mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        btn_bottom_sheet_dialog_control.setOnClickListener {
            if (mBottomSheetDialog!!.isShowing) {
                mBottomSheetDialog?.dismiss()
            } else {
                mBottomSheetDialog?.show()
            }
        }
        createBottomSheetDialog()
    }

    private fun createBottomSheetDialog() {
        mBottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.single_recycler_view, window.decorView as ViewGroup, false)
        mBottomSheetDialog?.setContentView(view)
        BottomSheetBehavior.from(view.parent as View).peekHeight = 750  // 设置默认弹出的高度

        view.findViewById<RecyclerView>(R.id.recyclerView).run {
            layoutManager = LinearLayoutManager(this@BottomSheetActivity, LinearLayoutManager.VERTICAL, false)
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