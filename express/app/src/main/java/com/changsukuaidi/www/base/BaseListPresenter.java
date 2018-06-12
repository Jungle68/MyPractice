package com.changsukuaidi.www.base;

import com.changsukuaidi.www.base.i.IBaseListPresenter;
import com.changsukuaidi.www.base.i.IBaseView;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhiyicx
 * @Date 2017/12/1
 * @Contact
 */

public class BaseListPresenter<R,V extends IBaseView> extends BasePresenter<R,V> implements IBaseListPresenter {

    @Inject
    public BaseListPresenter(V view, R repository, BaseApplication application) {
        super(view, repository, application);
    }

    @Override
    public void requestListData(boolean isLoadMore, int page) {

    }
}
