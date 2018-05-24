package com.changsukuaidi.www.adapter;

import android.content.Context;

import com.changsukuaidi.www.adapter.delegate.AlbumDetailDelegate;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;

import java.util.List;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/19
 */

public class AlbumDetailAdapter extends MultiItemTypeAdapter<PhotoBean> {

    public AlbumDetailAdapter(Context context, List<PhotoBean> datas, int column, boolean showCheckBox) {
        super(context, datas);
        addItemViewDelegate(new AlbumDetailDelegate(context, column, showCheckBox));
    }
}
