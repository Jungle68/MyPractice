package com.changsukuaidi.www.modules.task.tripartite_dispatch;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.modules.task.TaskRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/7
 * @Contact 605626708@qq.com
 */

class TripartitePresenter extends BasePresenter<TaskRepository, ITripartiteView> {

    @Inject
    TripartitePresenter(ITripartiteView view, TaskRepository repository, BaseApplication application) {
        super(view, repository, application);
    }
}
