package com.changsukuaidi.www.modules.task.list;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseListPresenter;
import com.changsukuaidi.www.bean.TaskBean;
import com.changsukuaidi.www.modules.task.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class TaskListPresenter extends BaseListPresenter<TaskRepository, ITaskListView> {

    @Inject
    TaskListPresenter(ITaskListView view, TaskRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    @Override
    public void requestListData(boolean isLoadMore, int page) {
        int type = mRootView.getTaskType();
        List<TaskBean> beanList = new ArrayList<>();
        if (type == 0) {
            beanList.add(new TaskBean(1, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 1));
            beanList.add(new TaskBean(1, 1, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(2, 0, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(2, 0, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(3, 0, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(4, 0, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(4, 0, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(5, 0, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(5, 0, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        } else if (type == 1) {
            beanList.add(new TaskBean(1, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 1));
            beanList.add(new TaskBean(2, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(2, 1, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        } else if (type == 2) {
            beanList.add(new TaskBean(1, 1, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 1));
            beanList.add(new TaskBean(1, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(1, 0, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(1, 0, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        } else if (type == 3) {
            beanList.add(new TaskBean(2, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 1));
            beanList.add(new TaskBean(2, 0, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        } else if (type == 4 ) {
            beanList.add(new TaskBean(3, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(3, 0, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        } else if (type == 5 || type == 6) {
            beanList.add(new TaskBean(4, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(4, 0, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        } else if (type == 7) {
            beanList.add(new TaskBean(5, 1, 1, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
            beanList.add(new TaskBean(5, 0, 2, "成都市市人民医院", 0, 0, "四川省成都市高新西区西苑122号3栋02233", 0, 0, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        }
        mRootView.loadDataSuccess(isLoadMore, beanList);
    }
}
