package com.thc.www.p2p.module

import com.thc.www.p2p.android.home.apk.ApkFragment
import com.thc.www.p2p.annotation.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Module
class ApkModule(private val view: ApkFragment) {
    @FragmentScope
    @Provides
    fun provideView() = view
}