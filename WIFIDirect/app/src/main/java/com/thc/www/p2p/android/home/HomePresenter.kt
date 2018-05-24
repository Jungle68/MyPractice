package com.thc.www.p2p.android.home

import com.thc.www.p2p.base.BasePresenter
import javax.inject.Inject


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class HomePresenter @Inject constructor(repository: HomeRepository, view: HomeFragment)
    : BasePresenter<HomeRepository, HomeFragment>(repository, view) {
}