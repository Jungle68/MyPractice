package com.changsukuaidi.www.modules.login.launcher;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/29
 */

public class LauncherActivity extends BaseActivity<LauncherFragment> {

    @Override
    protected LauncherFragment getFragment() {
        return new LauncherFragment();
    }

    @Override
    protected void initData() {

    }
}
