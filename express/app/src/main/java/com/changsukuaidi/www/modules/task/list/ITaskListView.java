package com.changsukuaidi.www.modules.task.list;

import com.changsukuaidi.www.base.i.IBaseListView;
import com.changsukuaidi.www.bean.TaskBean;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public interface ITaskListView extends IBaseListView<TaskBean> {
    int getTaskType();
}
