package com.changsukuaidi.www.dagger.module;


import com.changsukuaidi.www.base.BaseApplication;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/12/1
 * @Contact 605626708@qq.com
 */

@Module
public class BaseModule {
    private static BaseApplication mBaseApplication;

    public BaseModule(BaseApplication baseApplication) {
        mBaseApplication = baseApplication;
    }

    @Provides
    static BaseApplication provideBaseApplication() {
        return mBaseApplication;
    }
}
