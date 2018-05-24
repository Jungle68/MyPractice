package com.thc.www.p2p.android.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.thc.www.p2p.android.home.apk.ApkFragment
import com.thc.www.p2p.android.home.photo.PhotoFragment
import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.comp.DaggerHomeComp
import com.thc.www.p2p.module.HomeModule
import com.thc.www.p2p.utils.DisplayUtils
import com.thc.www.wifi_direct.R
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class HomeFragment : BaseFragment<HomePresenter>() {
    private val fragments = ArrayList<Fragment>().apply {
        add(ApkFragment())
        add(PhotoFragment())
    }
    private val titles = arrayOf("已安装应用", "相册")

    override fun setPresenter() {
        DaggerHomeComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .homeModule(HomeModule(this))
                .build()
                .injectMembers(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        vp_home_bottom.apply {
            adapter = object : FragmentStatePagerAdapter(fragmentManager) {
                override fun getItem(position: Int): Fragment = fragments[position]

                override fun getCount(): Int = fragments.size

                override fun getPageTitle(position: Int): CharSequence? = titles[position]
            }

            offscreenPageLimit = fragments.size - 1
        }
        tl_tab.setupWithViewPager(vp_home_bottom, true)
    }

    override fun fullScreen(): Boolean = true
}