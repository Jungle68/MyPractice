package com.thc.www.p2p.android.login

import com.thc.www.p2p.base.BaseActivity

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class LoginActivity : BaseActivity<LoginFragment>() {
    override fun getFragment(): LoginFragment = LoginFragment()
}