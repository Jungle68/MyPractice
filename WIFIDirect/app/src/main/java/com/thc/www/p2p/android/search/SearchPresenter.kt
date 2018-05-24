package com.thc.www.p2p.android.search

import com.thc.www.p2p.base.BasePresenter
import javax.inject.Inject

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class SearchPresenter @Inject constructor(repository: SearchRepository, view: SearchFragment)
    : BasePresenter<SearchRepository, SearchFragment>(repository, view) {

}