package com.thc.www.p2p.android.launcher

import com.thc.www.p2p.base.BaseActivity

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class LauncherActivity : BaseActivity<LauncherFragment>() {
    override fun getFragment(): LauncherFragment = LauncherFragment()
}