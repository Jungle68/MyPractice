package com.changsukuaidi.www.base;

import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.i.IBaseListPresenter;
import com.changsukuaidi.www.bean.BaseBean;
import com.changsukuaidi.www.databinding.FragmentNestedBaseListBinding;

import java.util.List;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/11/30
 * @Contact 605626708@qq.com
 */

public abstract class BaseNestedListFragment<P extends IBaseListPresenter, D extends BaseBean> extends ABaseListFragment<P, D, FragmentNestedBaseListBinding>{

    @Override
    protected int layoutId() {
        return R.layout.fragment_nested_base_list;
    }

    @Override
    protected void initLayout() {
        mRefreshLayout = mViewBinding.refreshLayout;
        mEmptyLayout = mViewBinding.emptyLayout;
        mRecyclerView = mViewBinding.recyclerView;
    }

    @Override
    public void loadDataSuccess(boolean isLoadMore, List<D> data) {
        super.loadDataSuccess(isLoadMore, data);
        mViewBinding.nes.setVisibility(!isLoadMore && data.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
