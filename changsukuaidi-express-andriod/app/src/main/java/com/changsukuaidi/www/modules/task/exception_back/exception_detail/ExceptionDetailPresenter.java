package com.changsukuaidi.www.modules.task.exception_back.exception_detail;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.modules.task.TaskRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/6
 * @Contact 605626708@qq.com
 */

class ExceptionDetailPresenter extends BasePresenter<TaskRepository, IExceptionDetailView> {

    @Inject
    ExceptionDetailPresenter(IExceptionDetailView view, TaskRepository repository, BaseApplication application) {
        super(view, repository, application);
    }
}
