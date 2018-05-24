package com.changsukuaidi.www.photo_pick;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.bean.PhotoBean;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class PhotoAlbumDetailPresenter extends BasePresenter<PhotoAlbumRepository, PhotoAlbumDetailView> {
    private boolean isSingleMode; // 是否是单选模式

    @Inject
    PhotoAlbumDetailPresenter(PhotoAlbumDetailView view, PhotoAlbumRepository repository, boolean isSingleMode, BaseApplication application) {
        super(view, repository, application);
        this.isSingleMode = isSingleMode;
    }

    void onSelectPhoto(ArrayList<PhotoBean> photos, PhotoBean selectPhoto, int canChooseCount) {
        if (isSingleMode) { // 单张选择模式

        } else if (photos.contains(selectPhoto)) { // 取消选中
            photos.remove(selectPhoto);
            mRootView.selectPhoto(false, selectPhoto);
        } else {
            if (photos.size() == canChooseCount) { // 达到最大选择张数
                mRootView.selectPhoto(false, null);
            } else { // 选中图片
                photos.add(selectPhoto);
                mRootView.selectPhoto(true, selectPhoto);
            }
        }
    }
}
