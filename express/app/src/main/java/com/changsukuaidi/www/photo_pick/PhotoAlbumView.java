package com.changsukuaidi.www.photo_pick;

import com.changsukuaidi.www.base.i.IBaseView;
import com.changsukuaidi.www.bean.AlbumBean;

import java.util.ArrayList;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/2
 */

public interface PhotoAlbumView extends IBaseView {
    void finishLoadAlbumDatas(ArrayList<AlbumBean> albumBeanList);
}
