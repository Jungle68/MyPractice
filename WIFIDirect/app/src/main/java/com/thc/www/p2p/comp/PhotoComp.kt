package com.thc.www.p2p.comp

import com.thc.www.p2p.android.home.photo.PhotoFragment
import com.thc.www.p2p.annotation.FragmentScope
import com.thc.www.p2p.module.PhotoModule
import dagger.Component
import dagger.MembersInjector

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@FragmentScope
@Component(dependencies = [AppComp::class], modules = [PhotoModule::class])
interface PhotoComp : MembersInjector<PhotoFragment>