package com.changsukuaidi.www.modules.punch;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class PunchActivity extends BaseActivity<PunchFragment> {
    private boolean isOffWork = false;

    @Override
    protected PunchFragment getFragment() {
        if (isOffWork)
            return new OffWorkPunchFragment();
        return new StartPunchFragment();
    }

    @Override
    protected void initIntent() {
        isOffWork = getIntent().getBooleanExtra("is_off_work", false);
    }

    @Override
    protected void initData() {

    }
}
