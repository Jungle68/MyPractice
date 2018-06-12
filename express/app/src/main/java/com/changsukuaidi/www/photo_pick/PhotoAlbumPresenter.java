package com.changsukuaidi.www.photo_pick;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.bean.AlbumBean;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class PhotoAlbumPresenter extends BasePresenter<PhotoAlbumRepository, PhotoAlbumView> {
    @Inject
    PhotoAlbumPresenter(PhotoAlbumView view, PhotoAlbumRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    void loadAlbumDatas(FragmentActivity activity) {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(MediaStoreHelper.SHOW_GIF, true);
        MediaStoreHelper.getPhotoDirs(activity, mediaStoreArgs,
                dirs -> mRootView.finishLoadAlbumDatas((ArrayList<AlbumBean>) dirs));
    }
}
