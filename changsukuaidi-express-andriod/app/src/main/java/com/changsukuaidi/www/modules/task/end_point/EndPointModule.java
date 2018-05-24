package com.changsukuaidi.www.modules.task.end_point;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

@Module
class EndPointModule {

    private final IEndPointView mView;

    EndPointModule(IEndPointView view) {
        mView = view;
    }

    @Provides
    IEndPointView provideHomeView(){
        return mView;
    }
}
