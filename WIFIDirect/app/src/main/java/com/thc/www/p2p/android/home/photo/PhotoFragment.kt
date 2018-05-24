package com.thc.www.p2p.android.home.photo

import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.comp.DaggerPhotoComp
import com.thc.www.p2p.module.PhotoModule
import com.thc.www.wifi_direct.R

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class PhotoFragment : BaseFragment<PhotoPresenter>() {
    override fun getLayoutId(): Int = R.layout.fragment_home_photo


    override fun setPresenter() {
        DaggerPhotoComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .photoModule(PhotoModule(this))
                .build()
                .injectMembers(this)
    }

    override fun initView() {

    }

    override fun fullScreen(): Boolean = true
}