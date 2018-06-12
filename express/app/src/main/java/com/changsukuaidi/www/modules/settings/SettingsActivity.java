package com.changsukuaidi.www.modules.settings;

import android.content.Intent;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class SettingsActivity extends BaseActivity<SettingsFragment> {
    private SettingsFragment mSettingsFragment;
    @Override
    protected SettingsFragment getFragment() {
        mSettingsFragment = new SettingsFragment();
        return mSettingsFragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSettingsFragment.onActivityResult(requestCode, resultCode, data);
    }
}
