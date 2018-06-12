package com.changsukuaidi.www.modules.task.list;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
public class TaskListModule {
    private final ITaskListView mView;

    TaskListModule(ITaskListView view) {
        mView = view;
    }

    @Provides
    ITaskListView provideTaskListView(){
        return mView;
    }
}
