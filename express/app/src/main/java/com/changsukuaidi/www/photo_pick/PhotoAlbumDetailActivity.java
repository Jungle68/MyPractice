package com.changsukuaidi.www.photo_pick;

import com.changsukuaidi.www.base.BaseActivity;
import com.changsukuaidi.www.bean.AlbumBean;

import java.util.ArrayList;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class PhotoAlbumDetailActivity extends BaseActivity<PhotoAlbumDetailFragment> {
    private int mPosition;
    private ArrayList<AlbumBean> mAlbumList;
    private boolean isSingleMode = true;
    private int mCanChooseCount; // 还能选择的相片数

    @Override
    protected PhotoAlbumDetailFragment getFragment() {
        return PhotoAlbumDetailFragment.newInstance(mPosition, mAlbumList, isSingleMode, mCanChooseCount);
    }

    @Override
    protected void initIntent() {
        if (getIntent().getExtras() != null) {
            mPosition = getIntent().getExtras().getInt(PhotoAlbumFragment.SELECTED_DIRECTORY_NUMBER, 0);
            mAlbumList = getIntent().getExtras().getParcelableArrayList(PhotoAlbumFragment.ALL_PHOTOS);
            isSingleMode = getIntent().getExtras().getBoolean(PhotoAlbumDetailFragment.ONLY_SELECT_ONE_PHOTO);
            mCanChooseCount = getIntent().getIntExtra(PhotoAlbumDetailFragment.CAN_SELECT_COUNT, PhotoAlbumDetailFragment.MAX_SELECTED_COUNT);
        }
    }

    @Override
    protected void initData() {

    }
}
