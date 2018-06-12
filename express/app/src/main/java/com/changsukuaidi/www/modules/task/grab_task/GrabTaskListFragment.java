package com.changsukuaidi.www.modules.task.grab_task;

import android.support.v7.widget.RecyclerView;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseListFragment;
import com.changsukuaidi.www.bean.TaskBean;
import com.changsukuaidi.www.utils.recycler_itemdecoration.MyDividerItemDecoration;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class GrabTaskListFragment extends BaseListFragment<GrabTaskListPresenter, TaskBean> implements IGrabTaskView {

    @Override
    protected void initIntent() {
        super.initIntent();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new GrabTaskAdapter(getContext(), mListDatas);
    }

    @Override
    protected void initView() {
        super.initView();
        setToolbarTitle("抢单列表");
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        MyDividerItemDecoration myDividerItemDecoration = new MyDividerItemDecoration(getContext(), MyDividerItemDecoration.VERTICAL);
        myDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.space_height_drwable));
        return myDividerItemDecoration;
    }

    @Override
    protected void setPresenter() {
        DaggerGrabTaskListComp.builder()
                .appComp(BaseApplication.getAppComp())
                .grabTaskListModule(new GrabTaskListModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    public boolean useTitle() {
        return true;
    }
}
