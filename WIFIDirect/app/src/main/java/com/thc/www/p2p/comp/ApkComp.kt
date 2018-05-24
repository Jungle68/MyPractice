package com.thc.www.p2p.comp

import com.thc.www.p2p.android.home.apk.ApkFragment
import com.thc.www.p2p.annotation.FragmentScope
import com.thc.www.p2p.module.ApkModule
import dagger.Component
import dagger.MembersInjector

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@FragmentScope
@Component(dependencies = [AppComp::class], modules = [ApkModule::class])
interface ApkComp : MembersInjector<ApkFragment>