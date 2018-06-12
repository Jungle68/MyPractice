package com.changsukuaidi.www.photo_pick;

import com.changsukuaidi.www.base.i.IBaseView;
import com.changsukuaidi.www.bean.PhotoBean;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/2
 */

public interface PhotoAlbumDetailView extends IBaseView {
    void selectPhoto(boolean select, PhotoBean photo);
}
