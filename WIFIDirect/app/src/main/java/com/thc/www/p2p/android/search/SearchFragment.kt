package com.thc.www.p2p.android.search

import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.comp.DaggerSearchComp
import com.thc.www.p2p.module.SearchModule
import com.thc.www.wifi_direct.R

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class SearchFragment : BaseFragment<SearchPresenter>() {
    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun setPresenter() {
        DaggerSearchComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .searchModule(SearchModule(this))
                .build()
                .injectMembers(this)
    }

    override fun fullScreen(): Boolean = true
}