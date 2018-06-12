package com.changsukuaidi.www.modules.task.detail;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.databinding.FragmentTaskDetailBinding;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/8
 * @Contact 605626708@qq.com
 */

public class TaskDetailFragment extends BaseFragment<TaskDetailPresenter, FragmentTaskDetailBinding> implements ITaskDetailView {

    @Override
    protected void setPresenter() {
        DaggerTaskDetailComp.builder()
                .appComp(BaseApplication.getAppComp())
                .taskDetailModule(new TaskDetailModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_task_detail;
    }
}
