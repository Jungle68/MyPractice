package com.thc.www.p2p.android.home.apk

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.bean.AppInfoBean
import com.thc.www.p2p.comp.DaggerApkComp
import com.thc.www.p2p.module.ApkModule
import com.thc.www.p2p.utils.DisplayUtils
import com.thc.www.wifi_direct.R
import kotlinx.android.synthetic.main.fragment_home_apk.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class ApkFragment : BaseFragment<ApkPresenter>() {
    companion object {
        private const val MAX_SPAN_COUNT = 4
    }

    private val appInfoList = arrayListOf<AppInfoBean>()
    override fun getLayoutId(): Int = R.layout.fragment_home_apk

    override fun setPresenter() {
        DaggerApkComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .apkModule(ApkModule(this))
                .build()
                .injectMembers(this)
    }

    override fun initView() {
        mPresenter?.loadAppInfo()
        rv_app.layoutManager = GridLayoutManager(context, MAX_SPAN_COUNT)
        rv_app.adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                    ViewHolder(layoutInflater.inflate(R.layout.item_home_rv, parent, false))

            override fun getItemCount(): Int = appInfoList.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)
        }
    }

    override fun fullScreen(): Boolean = true

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val imgAppIcon = rootView.findViewById<ImageView>(R.id.iv_app_icon)
        private val tvAppName = rootView.findViewById<TextView>(R.id.tv_app_name)

        init {
            rootView.apply {
                layoutParams.width = rv_app.width / MAX_SPAN_COUNT
                layoutParams.height = rv_app.width / MAX_SPAN_COUNT
                setPadding(DisplayUtils.dp2px(context, 10),
                        DisplayUtils.dp2px(context, 10),
                        DisplayUtils.dp2px(context, 10),
                        DisplayUtils.dp2px(context, 10))
            }
        }

        fun bindView(position: Int) {
            val appInfo = appInfoList[position]
            Glide.with(context!!)
                    .load(appInfo.apkIcon)
                    .apply(RequestOptions().placeholder(R.mipmap.ic_launcher))
                    .into(imgAppIcon)
            tvAppName.text = appInfo.appName
        }
    }

    fun finishLoadAppInfo(appInfos: ArrayList<AppInfoBean>) {
        appInfoList.clear()
        appInfoList.addAll(appInfos)
        rv_app.adapter.notifyDataSetChanged()
    }
}