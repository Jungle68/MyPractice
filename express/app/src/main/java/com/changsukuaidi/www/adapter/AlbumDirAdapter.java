package com.changsukuaidi.www.adapter;

import android.content.Context;

import com.changsukuaidi.www.adapter.delegate.AlbumDirDelegate;
import com.changsukuaidi.www.bean.AlbumBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;

import java.util.List;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/19
 */

public class AlbumDirAdapter extends MultiItemTypeAdapter<AlbumBean> {

    public AlbumDirAdapter(Context context, List<AlbumBean> datas) {
        super(context, datas);
        addItemViewDelegate(new AlbumDirDelegate());
    }
}
