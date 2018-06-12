package com.changsukuaidi.www.modules.settings;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
public class SettingsModule {
    private final SettingsView mView;

    SettingsModule(SettingsView view) {
        mView = view;
    }

    @Provides
    SettingsView provideLoginView(){
        return mView;
    }
}
