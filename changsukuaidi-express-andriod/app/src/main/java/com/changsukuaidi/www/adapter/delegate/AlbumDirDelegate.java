package com.changsukuaidi.www.adapter.delegate;

import android.widget.ImageView;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.AlbumBean;
import com.changsukuaidi.www.common.widget.recyclerview.base.ItemViewDelegate;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/20
 */

public class AlbumDirDelegate implements ItemViewDelegate<AlbumBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_album;
    }

    @Override
    public boolean isForViewType(AlbumBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, AlbumBean albumBean, int position) {
        holder.setText(R.id.tv_dir_name, albumBean.getName());
        holder.setText(R.id.tv_dir_count, albumBean.getPhotos().size() + "张");
        GlideApp.with(holder.getConvertView().getContext())
                .load(albumBean.getCoverPath())
                .into((ImageView) holder.getConvertView().findViewById(R.id.iv_dir_cover));
    }
}
