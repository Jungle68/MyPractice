package com.thc.www.p2p.module

import com.thc.www.p2p.android.login.LoginFragment
import com.thc.www.p2p.annotation.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Module
class LoginModule(private val view: LoginFragment) {
    @FragmentScope
    @Provides
    fun provideView() = view
}