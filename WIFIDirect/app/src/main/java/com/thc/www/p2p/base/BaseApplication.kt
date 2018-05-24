package com.thc.www.p2p.base

import android.app.Application
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.thc.www.p2p.comp.AppComp
import com.thc.www.p2p.comp.DaggerAppComp
import com.thc.www.p2p.module.AppModule
import com.thc.www.wifi_direct.R

/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
class BaseApplication : Application() {
    private val mAppComp: AppComp

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, layout ->
            layout.setPrimaryColorsId(android.R.color.white, R.color.colorPrimary)//全局设置主题颜色
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, _ -> ClassicsFooter(context).setDrawableSize(20f) }
        mAppComp = DaggerAppComp.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getAppComp() = mAppComp
}