package com.changsukuaidi.www.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.changsukuaidi.www.base.i.IBaseListPresenter;
import com.changsukuaidi.www.base.i.IBaseListView;
import com.changsukuaidi.www.bean.BaseBean;
import com.changsukuaidi.www.common.widget.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.changsukuaidi.www.utils.ConvertUtils;
import com.changsukuaidi.www.utils.recycler_itemdecoration.LinearDecoration;
import com.changsukuaidi.www.widget.empty_layout.EmptyLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/11/30
 * @Contact 605626708@qq.com
 */

public abstract class ABaseListFragment<P extends IBaseListPresenter, D extends BaseBean, VB extends ViewDataBinding> extends BaseFragment<P, VB> implements IBaseListView<D>, OnRefreshListener, OnLoadmoreListener {

    protected SmartRefreshLayout mRefreshLayout;
    protected EmptyLayout mEmptyLayout;
    protected RecyclerView mRecyclerView;

    protected RecyclerView.Adapter mAdapter;
    protected HeaderAndFooterWrapper mWrapper;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<D> mListDatas = new ArrayList<>();
    public static final float DEFAULT_LIST_ITEM_SPACING = 0.5f;
    public static final int DEFAULT_MAX_ITEMS = 15;
    private int mMaxId;

    /**
     * 给layout赋值
     */
    protected abstract void initLayout();

    @Override
    protected void initView() {

        initLayout();

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mAdapter = getAdapter();
        mLayoutManager = getmLayoutManager();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);
        mRecyclerView.setAdapter(mWrapper);
        mRecyclerView.addItemDecoration(getItemDecoration());
        mEmptyLayout.setState(EmptyLayout.HIDE_LAYOUT);
        mEmptyLayout.setOnEmptyOrErrorClicked(getOnEmptyOrErrorClicked());
    }

    @Override
    protected void initData() {
        mEmptyLayout.setState(EmptyLayout.LOADING);
        mPresenter.requestListData(false, mMaxId = 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPresenter.requestListData(false, mMaxId = 1);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPresenter.requestListData(true, getPageCount());
    }

    protected abstract RecyclerView.Adapter getAdapter();

    protected RecyclerView.LayoutManager getmLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new LinearDecoration(0, ConvertUtils.dp2px(getActivity(), getItemDecorationSpacing()), 0, 0);
    }

    protected float getItemDecorationSpacing() {
        return DEFAULT_LIST_ITEM_SPACING;
    }

    @Override
    public void loadDataSuccess(boolean isLoadMore, List<D> data) {
        mEmptyLayout.setState(EmptyLayout.HIDE_LAYOUT);
        if (!isLoadMore) {
            mRefreshLayout.finishRefresh();
            if (data.isEmpty()) {
                mEmptyLayout.setState(EmptyLayout.NODATA);
                mRefreshLayout.setEnableLoadmore(false);
            } else {
                mListDatas.clear();
                mListDatas.addAll(data);
                mWrapper.notifyDataSetChanged();
                mRefreshLayout.setEnableLoadmore(data.size() >= getPageItems());
            }
        } else {
            mRefreshLayout.finishLoadmore();
            mListDatas.addAll(data);
            mWrapper.notifyDataSetChanged();
            mRefreshLayout.setEnableLoadmore(data.size() >= getPageItems());
        }
    }

    @Override
    public void loadDataError(boolean isLoadMore) {
        if (!isLoadMore) {
            mRefreshLayout.finishRefresh(false);
            mEmptyLayout.setState(EmptyLayout.ERROR);
        } else {
            mRefreshLayout.finishLoadmore(false);
        }
    }

    /**
     * 空/error时点击后
     */
    protected View.OnClickListener getOnEmptyOrErrorClicked() {
        return v -> {
            mEmptyLayout.setState(EmptyLayout.LOADING);
            onRefresh(mRefreshLayout);
        };
    }

    /**
     * 获取当前分页每页条数
     */
    @Override
    public int getPageItems() {
        return DEFAULT_MAX_ITEMS;
    }

    /**
     * 获取分页标记
     */
    public int getPageCount() {
        return mListDatas != null ? mListDatas.size() / getPageItems() + 1 : 1;
    }
}
