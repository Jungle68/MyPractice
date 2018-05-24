package com.changsukuaidi.www.modules.task.history;

import android.view.View;
import android.view.ViewGroup;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.PageAdapterWithTitle;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.modules.task.MyTaskFragment;
import com.changsukuaidi.www.modules.task.list.TaskListFragment;

import java.util.ArrayList;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/7
 * @Contact 605626708@qq.com
 */

public class HistoryFragment extends MyTaskFragment {

    @Override
    protected void initView() {
        setToolbarTitle(getString(R.string.history_task));
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mFragments.add(TaskListFragment.newInstance(6 + i));
        }
        mViewBinding.vp.setAdapter(new PageAdapterWithTitle<>(getChildFragmentManager(), mFragments,
                getResources().getStringArray(R.array.history_task_type)));

        mViewBinding.tabs.setupWithViewPager(mViewBinding.vp);

        for (int i = 0; i < mViewBinding.tabs.getTabCount(); i++) {
            View tab = ((ViewGroup) mViewBinding.tabs.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            layoutParams.setMargins(DisplayUtils.dp2px(getContext(), 40), 0, DisplayUtils.dp2px(getContext(), 40), 0);
        }
        mViewBinding.tabs.requestLayout();

        // TODO: 2018/3/2
        // NAME: zhouhao
        // DESC: set custom Tab view
    }
}
