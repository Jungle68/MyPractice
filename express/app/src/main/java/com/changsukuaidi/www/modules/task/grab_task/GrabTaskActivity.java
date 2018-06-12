package com.changsukuaidi.www.modules.task.grab_task;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/7
 * @Contact 605626708@qq.com
 */

public class GrabTaskActivity extends BaseActivity<GrabTaskListFragment> {
    @Override
    protected GrabTaskListFragment getFragment() {
        return new GrabTaskListFragment();
    }

    @Override
    protected void initData() {

    }
}
