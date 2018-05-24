package com.changsukuaidi.www.modules.task.grab_task;

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

public class GrabTaskListPresenter extends BaseListPresenter<TaskRepository, IGrabTaskView> {

    @Inject
    GrabTaskListPresenter(IGrabTaskView view, TaskRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    @Override
    public void requestListData(boolean isLoadMore, int page) {
        List<TaskBean> beanList = new ArrayList<>();
        beanList.add(new TaskBean(1, 1, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 1));
        beanList.add(new TaskBean(1, 1, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        beanList.add(new TaskBean(1, 1, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        beanList.add(new TaskBean(1, 0, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        beanList.add(new TaskBean(1, 0, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        beanList.add(new TaskBean(1, 0, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        beanList.add(new TaskBean(1, 0, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        beanList.add(new TaskBean(1, 0, 1, "成都市市人民医院", 30.579236547019285, 104.065567207951370, "四川省成都市高新西区西苑122号3栋02233", 30.56349531019283, 104.0687472094567, 0.2F, 2, "顾古古", "13456781234", "2017-12-06 12:34:12", 0));
        mRootView.loadDataSuccess(isLoadMore, beanList);
    }
}
