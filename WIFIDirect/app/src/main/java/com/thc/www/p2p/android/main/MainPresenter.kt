package com.thc.www.p2p.android.main

import com.thc.www.p2p.base.BasePresenter
import javax.inject.Inject

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class MainPresenter @Inject constructor(repository: MainRepository, view: MainFragment)
    : BasePresenter<MainRepository, MainFragment>(repository, view) {


}