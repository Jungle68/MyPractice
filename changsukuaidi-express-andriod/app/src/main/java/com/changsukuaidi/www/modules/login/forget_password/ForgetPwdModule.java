package com.changsukuaidi.www.modules.login.forget_password;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class ForgetPwdModule {
    private final ForgetPwdContact.ForgetPwdView mView;

    ForgetPwdModule(ForgetPwdContact.ForgetPwdView view) {
        mView = view;
    }

    @Provides
    ForgetPwdContact.ForgetPwdView provideForgetPwdView(){
        return mView;
    }
}
