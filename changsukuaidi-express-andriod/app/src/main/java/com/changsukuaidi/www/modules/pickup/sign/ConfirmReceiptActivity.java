package com.changsukuaidi.www.modules.pickup.sign;

import android.content.Intent;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class ConfirmReceiptActivity extends BaseActivity<ConfirmReceiptFragment> {

    @Override
    protected ConfirmReceiptFragment getFragment() {
        return new ConfirmReceiptFragment();
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
