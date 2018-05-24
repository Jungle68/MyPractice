package com.changsukuaidi.www.modules.task.tripartite_dispatch;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class TripartiteModule {
    private final ITripartiteView mView;

    TripartiteModule(ITripartiteView view) {
        mView = view;
    }

    @Provides
    ITripartiteView provideTripartiteView(){
        return mView;
    }
}
