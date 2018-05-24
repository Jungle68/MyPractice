package com.thc.www.p2p.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.thc.www.p2p.bean.TokenBean

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Database(entities = [TokenBean::class], version = 1)
abstract class UserDataBase : RoomDatabase() {
    abstract fun getTokenDao(): TokenDao
}