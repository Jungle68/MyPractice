package com.thc.www.p2p.comp

import com.thc.www.p2p.android.login.LoginFragment
import com.thc.www.p2p.annotation.FragmentScope
import com.thc.www.p2p.module.LoginModule
import dagger.Component
import dagger.MembersInjector

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@FragmentScope
@Component(dependencies = [AppComp::class], modules = [LoginModule::class])
interface LoginComp : MembersInjector<LoginFragment>