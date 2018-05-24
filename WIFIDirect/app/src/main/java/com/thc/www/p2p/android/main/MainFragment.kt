package com.thc.www.p2p.android.main

import android.support.v4.app.Fragment
import com.thc.www.p2p.adapter.PagerViewAdapter
import com.thc.www.p2p.android.history.HistoryFragment
import com.thc.www.p2p.android.home.HomeFragment
import com.thc.www.p2p.android.search.SearchFragment
import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.comp.DaggerMainComp
import com.thc.www.p2p.module.MainModule
import com.thc.www.wifi_direct.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class MainFragment : BaseFragment<MainPresenter>() {
    companion object {
        const val PAGE_COUNT = 3
        const val HOME_PAGE = 0
        const val HISTORY_PAGE = 1
        const val SEARCH_PAGE = 2
    }

    override fun setPresenter() {
        DaggerMainComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .mainModule(MainModule(this))
                .build()
                .injectMembers(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun initView() {
        ntvp_content.apply {
            adapter = PagerViewAdapter(fragmentManager).apply {
                bindDatas(initFragments())
            }
            offscreenPageLimit = PAGE_COUNT - 1
        }
        // 底部栏
        bnv_home_bottom_menu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> ntvp_content.setCurrentItem(HOME_PAGE, false)
                R.id.item_history -> ntvp_content.setCurrentItem(HISTORY_PAGE, false)
                R.id.item_search -> ntvp_content.setCurrentItem(SEARCH_PAGE, false)
                else -> throw UnknownError("No such id!")
            }
            true
        }
    }

    private fun initFragments(): ArrayList<Fragment> = arrayListOf(HomeFragment(), HistoryFragment(), SearchFragment())

    override fun fullScreen(): Boolean = true
}