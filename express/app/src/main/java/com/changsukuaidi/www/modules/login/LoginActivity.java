package com.changsukuaidi.www.modules.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class LoginActivity extends BaseActivity<LoginFragment> {
    @Override
    protected LoginFragment getFragment() {
        return new LoginFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.clearTaskStayOne(this);
    }
}
