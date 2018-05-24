package com.thc.www.p2p.module

import com.thc.www.p2p.android.home.HomeFragment
import com.thc.www.p2p.annotation.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Module
class HomeModule(private val view: HomeFragment) {
    @FragmentScope
    @Provides
    fun provideView() = view
}