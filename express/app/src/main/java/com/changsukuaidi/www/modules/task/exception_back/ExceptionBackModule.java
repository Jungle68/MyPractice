package com.changsukuaidi.www.modules.task.exception_back;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class ExceptionBackModule {
    private final IExceptionBackView mView;

    ExceptionBackModule(IExceptionBackView view) {
        mView = view;
    }

    @Provides
    IExceptionBackView provideExceptionBackView(){
        return mView;
    }
}
