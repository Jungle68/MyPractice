package com.changsukuaidi.www.modules.task.end_point;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.modules.task.TaskRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

class EndPointPresenter extends BasePresenter<TaskRepository, IEndPointView> {
    @Inject
    EndPointPresenter(IEndPointView view, TaskRepository repository, BaseApplication application) {
        super(view, repository, application);
    }
}
