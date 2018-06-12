package com.changsukuaidi.www.modules.task;

import android.view.Gravity;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.PageAdapterWithTitle;
import com.changsukuaidi.www.base.i.IBasePresenter;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.databinding.FragmentMyTaskBinding;
import com.changsukuaidi.www.databinding.WindowMyTaskBinding;
import com.changsukuaidi.www.modules.task.list.TaskListFragment;
import com.changsukuaidi.www.utils.ActivityUtils;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class MyTaskFragment extends BaseFragment<IBasePresenter, FragmentMyTaskBinding> implements CommonPopupWindow.ViewInterface<WindowMyTaskBinding> {

    protected List<TaskListFragment> mFragments;

    protected CommonPopupWindow mTaskPopupWindow;

    @Override
    protected void setPresenter() {

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_my_task;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolbarTitle(getString(R.string.my_task));
        mTitleViewBinding.toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_main_task, 0);
        mTitleViewBinding.toolbarTitle.setCompoundDrawablePadding(DisplayUtils.dp2px(getContext(), 12));

        // title 点击事件
        mTitleViewBinding.toolbarTitle.setOnClickListener(v -> {
            if (mTaskPopupWindow == null)
                mTaskPopupWindow = new CommonPopupWindow.Builder(getContext())
                        .setView(R.layout.window_my_task)
                        .setViewOnclickListener(this)
                        .create();
            ActivityUtils.setWindowBackgroundAlpha(getContext(), 0.5f);
            mTaskPopupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
        });

        mViewBinding.tabPriorBadge.setText("2");
        mViewBinding.tabWaitBadge.setText("6");
        mViewBinding.tabDispatchingBadge.setText("3");
        mViewBinding.tabExceptionalBadge.setText("6");

        mViewBinding.tabPriorBadge.setVisibility(View.VISIBLE);
        mViewBinding.tabWaitBadge.setVisibility(View.VISIBLE);
        mViewBinding.tabDispatchingBadge.setVisibility(View.VISIBLE);
        mViewBinding.tabExceptionalBadge.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        mFragments = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mFragments.add(TaskListFragment.newInstance(i));
        }
        mViewBinding.vp.setAdapter(new PageAdapterWithTitle<>(getChildFragmentManager(), mFragments,
                getResources().getStringArray(R.array.my_task_type)));
        mViewBinding.tabs.setupWithViewPager(mViewBinding.vp);

        // TODO: 2018/3/2
        // NAME: zhouhao
        // DESC: set custom Tab view
    }

    @Override
    public void getChildViewBinding(WindowMyTaskBinding view, int layoutResId) {
        RxView.clicks(view.materialDistribution)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mTitleViewBinding.toolbarTitle.setText(getString(R.string.material_distribution));
                    mTaskPopupWindow.dismiss();
                });

        RxView.clicks(view.drugTransit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mTitleViewBinding.toolbarTitle.setText(getString(R.string.drug_transit));
                    mTaskPopupWindow.dismiss();
                });

        RxView.clicks(view.drugDelivery)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    mTitleViewBinding.toolbarTitle.setText(getString(R.string.drug_delivery));
                    mTaskPopupWindow.dismiss();
                });
    }
}
