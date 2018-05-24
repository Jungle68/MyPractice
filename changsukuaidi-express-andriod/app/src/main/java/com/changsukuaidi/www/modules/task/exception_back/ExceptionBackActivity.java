package com.changsukuaidi.www.modules.task.exception_back;

import android.content.Intent;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/6
 * @Contact 605626708@qq.com
 */

public class ExceptionBackActivity extends BaseActivity<ExceptionBackFragment> {

    @Override
    protected ExceptionBackFragment getFragment() {
        return new ExceptionBackFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContainFragment.onActivityResult(requestCode, resultCode, data);
    }
}
