package com.thc.www.p2p.comp

import com.thc.www.p2p.android.home.HomeFragment
import com.thc.www.p2p.annotation.FragmentScope
import com.thc.www.p2p.module.HomeModule
import dagger.Component
import dagger.MembersInjector

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@FragmentScope
@Component(dependencies = [AppComp::class], modules = [HomeModule::class])
interface HomeComp : MembersInjector<HomeFragment>