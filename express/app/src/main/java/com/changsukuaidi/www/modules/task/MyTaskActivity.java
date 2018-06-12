package com.changsukuaidi.www.modules.task;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class MyTaskActivity extends BaseActivity<MyTaskFragment> {
    @Override
    protected MyTaskFragment getFragment() {
        return new MyTaskFragment();
    }

    @Override
    protected void initData() {

    }
}
