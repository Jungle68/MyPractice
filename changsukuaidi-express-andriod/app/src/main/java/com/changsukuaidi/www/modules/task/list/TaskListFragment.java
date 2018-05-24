package com.changsukuaidi.www.modules.task.list;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseListFragment;
import com.changsukuaidi.www.bean.TaskBean;
import com.changsukuaidi.www.scan.CaptureActivity;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.recycler_itemdecoration.MyDividerItemDecoration;

import io.reactivex.Observable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class TaskListFragment extends BaseListFragment<TaskListPresenter, TaskBean> implements ITaskListView {

    /**
     * 0: 全部, 1: 优先件, 2: 待取件, 3: 配送中, 4: 异常件, 5: 已完成, 6: 历史订单-已完成, 7: 历史订单-已取消
     */
    protected int type;

    public static TaskListFragment newInstance(int type) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Content.Extras.BOOLEAN, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean usePermissions() {
        return true;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        type = getArguments().getInt(Content.Extras.BOOLEAN);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new TaskAdapter(getContext(), mListDatas, type == 6 || type == 7, (type, taskBean) -> {
            if (type == 1) { // 扫描取件
                Observable.just(1)
                        .compose(mRxPermissions.ensure(Manifest.permission.CAMERA))
                        .subscribe(granted -> {
                            if (granted)
                                startActivity(new Intent(getActivity(), CaptureActivity.class));
                        });
            }
        });
    }

    @Override
    public int getTaskType() {
        return type;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        MyDividerItemDecoration myDividerItemDecoration = new MyDividerItemDecoration(getContext(), MyDividerItemDecoration.VERTICAL);
        myDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.space_height_drwable));
        return myDividerItemDecoration;
    }

    @Override
    protected void setPresenter() {
        DaggerTaskListComp.builder()
                .appComp(BaseApplication.getAppComp())
                .taskListModule(new TaskListModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    public boolean useTitle() {
        return false;
    }
}
