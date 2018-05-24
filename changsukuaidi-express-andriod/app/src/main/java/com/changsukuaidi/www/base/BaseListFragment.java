package com.changsukuaidi.www.base;

import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.i.IBaseListPresenter;
import com.changsukuaidi.www.bean.BaseBean;
import com.changsukuaidi.www.databinding.FragmentBaseListBinding;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/11/30
 * @Contact 605626708@qq.com
 */

public abstract class BaseListFragment<P extends IBaseListPresenter, D extends BaseBean> extends ABaseListFragment<P, D, FragmentBaseListBinding>{

    @Override
    protected int layoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initLayout() {
        mRefreshLayout = mViewBinding.refreshLayout;
        mEmptyLayout = mViewBinding.emptyLayout;
        mRecyclerView = mViewBinding.recyclerView;
    }

    @Override
    protected void initView() {
        super.initView();

        // 添加独立于刷新外的顶部view
        if (getTopView() != null && mViewBinding.topContainer != null) {
            mViewBinding.topContainer.addView(getTopView());
        }

        // 添加独立于刷新外的底部view
        if (getBottomView() != null && mViewBinding.bottomContainer != null) {
            mViewBinding.bottomContainer.addView(getBottomView());
        }
    }

    /**
     * 独立于smartRefreshLayout外的顶部View
     *
     * @return null will be ignored.
     */
    protected View getTopView() {
        return null;
    }

    /**
     * 独立于smartRefreshLayout外的底部View
     *
     * @return null will be ignored.
     */
    protected View getBottomView() {
        return null;
    }
}
