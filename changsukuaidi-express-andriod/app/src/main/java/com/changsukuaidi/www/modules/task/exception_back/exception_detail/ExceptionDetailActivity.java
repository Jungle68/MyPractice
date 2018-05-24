package com.changsukuaidi.www.modules.task.exception_back.exception_detail;

import com.changsukuaidi.www.base.BaseActivity;
import com.changsukuaidi.www.modules.task.exception_back.ExceptionBackFragment;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/6
 * @Contact 605626708@qq.com
 */

public class ExceptionDetailActivity extends BaseActivity<ExceptionDetailFragment> {

    @Override
    protected ExceptionDetailFragment getFragment() {
        return new ExceptionDetailFragment();
    }

    @Override
    protected void initData() {

    }
}
