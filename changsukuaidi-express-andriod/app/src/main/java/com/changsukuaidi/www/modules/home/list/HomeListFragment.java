package com.changsukuaidi.www.modules.home.list;

import android.support.v7.widget.RecyclerView;

import com.changsukuaidi.www.base.BaseListFragment;
import com.changsukuaidi.www.base.i.IBaseListPresenter;
import com.changsukuaidi.www.bean.BaseBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;

import java.util.ArrayList;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

public class HomeListFragment extends BaseListFragment<IBaseListPresenter, BaseBean> {
    @Override
    protected void setPresenter() {

    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<BaseBean>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>()) {
            @Override
            protected void convert(ViewHolder holder, BaseBean o, int position) {

            }
        };
    }
}
