package com.changsukuaidi.www.modules.punch;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class PunchModule {
    private final IPunchView mView;

    PunchModule(IPunchView view) {
        mView = view;
    }

    @Provides
    IPunchView providePunchView(){
        return mView;
    }
}
