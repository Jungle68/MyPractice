package com.changsukuaidi.www.modules.settings.modify_pwd;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class ModifyPwdModule {
    private final ModifyPwdView mView;

    ModifyPwdModule(ModifyPwdView view) {
        mView = view;
    }

    @Provides
    ModifyPwdView provideLoginView(){
        return mView;
    }
}
