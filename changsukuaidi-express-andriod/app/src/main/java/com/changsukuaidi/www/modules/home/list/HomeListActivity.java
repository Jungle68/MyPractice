package com.changsukuaidi.www.modules.home.list;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

public class HomeListActivity extends BaseActivity<HomeListFragment> {
    @Override
    protected HomeListFragment getFragment() {
        return new HomeListFragment();
    }

    @Override
    protected void initData() {

    }
}
