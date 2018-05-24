package com.thc.www.p2p.module

import com.thc.www.p2p.android.history.HistoryFragment
import com.thc.www.p2p.annotation.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Module
class HistoryModule(private val view: HistoryFragment) {
    @FragmentScope
    @Provides
    fun provideView() = view
}