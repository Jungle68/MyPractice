package com.thc.www.p2p.android.launcher

import com.thc.www.p2p.bean.TokenBean
import com.thc.www.p2p.dao.TokenDao
import javax.inject.Inject

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class LauncherRepository @Inject constructor(private val tokenDao: TokenDao) {
    fun getLoginInfo(): TokenBean {
        val tokenBeans: List<TokenBean> = tokenDao.getToken()
        return if (tokenBeans.isEmpty()) {
            TokenBean(0, "")
        } else {
            tokenBeans[0]
        }
    }
}