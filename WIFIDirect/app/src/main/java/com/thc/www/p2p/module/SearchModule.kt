package com.thc.www.p2p.module

import com.thc.www.p2p.android.search.SearchFragment
import com.thc.www.p2p.annotation.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Module
class SearchModule(private val view: SearchFragment) {
    @FragmentScope
    @Provides
    fun provideView() = view
}