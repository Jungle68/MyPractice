package com.changsukuaidi.www.modules.login.launcher;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class LauncherModule {
    private final LauncherFragment mView;

    LauncherModule(LauncherFragment view) {
        mView = view;
    }

    @Provides
    LauncherFragment provideLoginView(){
        return mView;
    }
}
