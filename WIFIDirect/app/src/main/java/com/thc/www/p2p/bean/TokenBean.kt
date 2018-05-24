package com.thc.www.p2p.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
@Entity(tableName = "token",indices = [(Index(value = ["id"], unique = true))])
data class TokenBean(@PrimaryKey @ColumnInfo(name = "id") var uid: Int,
                     @ColumnInfo(name = "token_info") var token: String)