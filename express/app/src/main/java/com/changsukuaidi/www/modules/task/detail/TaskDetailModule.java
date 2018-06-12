package com.changsukuaidi.www.modules.task.detail;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class TaskDetailModule {
    private final ITaskDetailView mView;

    TaskDetailModule(ITaskDetailView view) {
        mView = view;
    }

    @Provides
    ITaskDetailView provideTaskDetailView(){
        return mView;
    }
}
