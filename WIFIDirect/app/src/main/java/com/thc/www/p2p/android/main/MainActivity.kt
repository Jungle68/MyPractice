package com.thc.www.p2p.android.main

import com.thc.www.p2p.base.BaseActivity

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class MainActivity : BaseActivity<MainFragment>() {
    override fun getFragment(): MainFragment = MainFragment()
}