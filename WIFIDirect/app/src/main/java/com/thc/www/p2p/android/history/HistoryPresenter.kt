package com.thc.www.p2p.android.history

import com.thc.www.p2p.base.BasePresenter
import javax.inject.Inject

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class HistoryPresenter @Inject constructor(repository: HistoryRepository, view: HistoryFragment)
    : BasePresenter<HistoryRepository, HistoryFragment>(repository, view) {

}