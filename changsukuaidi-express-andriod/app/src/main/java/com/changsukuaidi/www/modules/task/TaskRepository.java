package com.changsukuaidi.www.modules.task;

import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.TaskClient;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class TaskRepository {

    private TaskClient mTaskClient;

    @Inject
    public TaskRepository(ClientManager clientManager) {
        mTaskClient = clientManager.provideTaskClient();
    }
}
