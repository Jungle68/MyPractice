package com.changsukuaidi.www.photo_pick;

import com.changsukuaidi.www.base.BaseActivity;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class PhotoAlbumActivity extends BaseActivity<PhotoAlbumFragment> {
    private boolean isSingleMode = true; // 是否是单选模式, 默认单选
    private int mCanChooseCount; // 还能选择的相片数

    @Override
    protected PhotoAlbumFragment getFragment() {
        return PhotoAlbumFragment.newInstance(isSingleMode, mCanChooseCount);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initIntent() {
        isSingleMode = getIntent().getBooleanExtra(PhotoAlbumDetailFragment.ONLY_SELECT_ONE_PHOTO, true);
        mCanChooseCount = getIntent().getIntExtra(PhotoAlbumDetailFragment.CAN_SELECT_COUNT, PhotoAlbumDetailFragment.MAX_SELECTED_COUNT);
    }
}
