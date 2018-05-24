package com.thc.www.p2p.module

import com.thc.www.p2p.base.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
@Module
class AppModule(application: BaseApplication) {
    private var app: BaseApplication = application

    @Singleton
    @Provides
    fun provideApp() = app
}