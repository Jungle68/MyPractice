package com.changsukuaidi.www.modules.colleague_pos;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class ColleaguePosModule {
    private final IColleaguePosView mView;

    ColleaguePosModule(IColleaguePosView view) {
        mView = view;
    }

    @Provides
    IColleaguePosView provideTaskDetailView(){
        return mView;
    }
}
