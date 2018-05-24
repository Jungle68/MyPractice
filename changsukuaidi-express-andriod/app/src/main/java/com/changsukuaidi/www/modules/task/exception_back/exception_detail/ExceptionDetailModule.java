package com.changsukuaidi.www.modules.task.exception_back.exception_detail;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class ExceptionDetailModule {
    private final IExceptionDetailView mView;

    ExceptionDetailModule(IExceptionDetailView view) {
        mView = view;
    }

    @Provides
    IExceptionDetailView provideExceptionBackView(){
        return mView;
    }
}
