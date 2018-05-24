package com.changsukuaidi.www.modules.login;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class LoginModule {
    private final LoginFragment mView;

    LoginModule(LoginFragment view) {
        mView = view;
    }

    @Provides
    LoginFragment provideLoginView(){
        return mView;
    }
}
