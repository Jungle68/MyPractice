package com.changsukuaidi.www.modules.home;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

@Module
class HomeModule {

    private final IHomeView mView;

    HomeModule(IHomeView view) {
        mView = view;
    }

    @Provides
    IHomeView provideHomeView(){
        return mView;
    }
}
