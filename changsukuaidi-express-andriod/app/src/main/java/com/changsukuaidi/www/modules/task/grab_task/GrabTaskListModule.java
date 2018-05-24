package com.changsukuaidi.www.modules.task.grab_task;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class GrabTaskListModule {
    private final IGrabTaskView mView;

    GrabTaskListModule(IGrabTaskView view) {
        mView = view;
    }

    @Provides
    IGrabTaskView provideTaskListView(){
        return mView;
    }
}
