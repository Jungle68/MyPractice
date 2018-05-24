package com.thc.www.p2p.module

import com.thc.www.p2p.dao.UserDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
@Module
class DaoModule {

    @Singleton
    @Provides
    fun provideTokenDao(userDataBase: UserDataBase) = userDataBase.getTokenDao()
}