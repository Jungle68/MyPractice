package com.thc.www.p2p.android.login

import com.thc.www.p2p.bean.TokenBean
import com.thc.www.p2p.dao.TokenDao
import javax.inject.Inject

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class LoginRepository @Inject constructor(private val tokenDao: TokenDao) {
    fun login(account: String, pwd: String): Boolean {
        return if (account.isEmpty() || pwd.isEmpty()) {
            false
        } else {
            tokenDao.insertToken(TokenBean(1, "qwerrt-24adqggcqq-1dasdas"))
            true
        }
    }
}