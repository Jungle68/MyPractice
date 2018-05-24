package com.changsukuaidi.www.base.i;

import java.util.List;

/**
 /**
 * @Describe
 * @Author zhiyicx
 * @Date 2017/12/1
 * @Contact
 */
public interface IBaseListView<D> extends IBaseView{

    /**
     * 加载数据成功
     * @param isLoadMore 是否为上拉加载
     * @param data 数据
     */
    void loadDataSuccess(boolean isLoadMore, List<D> data);

    /**
     * 加载数据失败
     * @param isLoadMore 是否为上拉加载
     */
    void loadDataError(boolean isLoadMore);

    /**
     * 获得单页最多item数
     */
    int getPageItems();
}
