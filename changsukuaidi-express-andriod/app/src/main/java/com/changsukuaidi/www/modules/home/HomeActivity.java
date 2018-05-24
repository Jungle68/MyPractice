package com.changsukuaidi.www.modules.home;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

public class HomeActivity extends BaseActivity<HomeFragment> {
    @Override
    protected HomeFragment getFragment() {
        return new HomeFragment();
    }

    @Override
    protected void initData() {

    }
}
