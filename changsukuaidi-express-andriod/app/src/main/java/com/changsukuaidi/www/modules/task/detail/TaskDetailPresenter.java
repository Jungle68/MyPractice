package com.changsukuaidi.www.modules.task.detail;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.modules.task.TaskRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/8
 * @Contact 605626708@qq.com
 */

class TaskDetailPresenter extends BasePresenter<TaskRepository, ITaskDetailView> {

    @Inject
    TaskDetailPresenter(ITaskDetailView view, TaskRepository repository, BaseApplication application) {
        super(view, repository, application);
    }
}
