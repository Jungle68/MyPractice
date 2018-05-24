package com.thc.www.p2p.android.launcher

import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.comp.DaggerLauncherComp
import com.thc.www.p2p.module.LauncherModule
import com.thc.www.wifi_direct.R

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class LauncherFragment : BaseFragment<LauncherPresenter>() {
    override fun setPresenter() {
        DaggerLauncherComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .launcherModule(LauncherModule(this))
                .build()
                .injectMembers(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_launcher

    override fun initData() {
        mPresenter?.judeToPage()
    }

    override fun fullScreen(): Boolean = true
}