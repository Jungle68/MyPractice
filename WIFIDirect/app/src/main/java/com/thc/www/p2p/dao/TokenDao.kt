package com.thc.www.p2p.dao

import android.arch.persistence.room.*
import com.thc.www.p2p.bean.TokenBean

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Dao
interface TokenDao {
    @Query("SELECT * FROM token")
    fun getToken(): List<TokenBean>

    @Query("SELECT * FROM token WHERE id = (:uid)")
    fun getTokenById(uid: Int): TokenBean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToken(vararg tokenBean: TokenBean)

    @Delete
    fun deleteToken(tokenBean: TokenBean)
}