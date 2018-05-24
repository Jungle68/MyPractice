package com.thc.www.p2p.android.history

import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.comp.DaggerHistoryComp
import com.thc.www.p2p.module.HistoryModule
import com.thc.www.wifi_direct.R

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class HistoryFragment : BaseFragment<HistoryPresenter>() {
    override fun getLayoutId(): Int = R.layout.fragment_history

    override fun setPresenter() {
        DaggerHistoryComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .historyModule(HistoryModule(this))
                .build()
                .injectMembers(this)
    }

    override fun fullScreen(): Boolean = true
}