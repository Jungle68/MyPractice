package com.thc.www.p2p.module

import android.arch.persistence.room.Room
import com.thc.www.p2p.base.BaseApplication
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
class DataBaseModule {

    @Singleton
    @Provides
    fun provideUserDataBase(application: BaseApplication) =
            Room.databaseBuilder(application, UserDataBase::class.java, "user.db").build()
}