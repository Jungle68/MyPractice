package com.thc.www.p2p.comp

import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.dao.TokenDao
import com.thc.www.p2p.dao.UserDataBase
import com.thc.www.p2p.module.AppModule
import com.thc.www.p2p.module.DaoModule
import com.thc.www.p2p.module.DataBaseModule
import dagger.Component
import javax.inject.Singleton

/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
@Singleton
@Component(modules = [AppModule::class, DataBaseModule::class, DaoModule::class])
interface AppComp {
    fun provideApp(): BaseApplication

    // 提供db对象
    fun provideUserDb(): UserDataBase

    // 提供dao对象
    fun provideTokenDao(): TokenDao
}