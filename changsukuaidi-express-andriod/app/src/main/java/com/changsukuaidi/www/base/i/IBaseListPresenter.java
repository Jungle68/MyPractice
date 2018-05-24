package com.changsukuaidi.www.base.i;

/**
 * @Describe
 * @Author zhiyicx
 * @Date 2017/12/1
 * @Contact
 */
public interface IBaseListPresenter extends IBasePresenter {

    void requestListData(boolean isLoadMore, int page);

}
