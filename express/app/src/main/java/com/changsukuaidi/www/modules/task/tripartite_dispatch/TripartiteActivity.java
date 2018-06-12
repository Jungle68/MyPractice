package com.changsukuaidi.www.modules.task.tripartite_dispatch;

import android.content.Intent;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/8
 * @Contact 605626708@qq.com
 */

public class TripartiteActivity extends BaseActivity<TripartiteFragment> {

    @Override
    protected TripartiteFragment getFragment() {
        return new TripartiteFragment();
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
