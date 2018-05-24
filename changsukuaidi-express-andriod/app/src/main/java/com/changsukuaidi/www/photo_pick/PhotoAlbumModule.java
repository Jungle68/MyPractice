package com.changsukuaidi.www.photo_pick;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/2
 */
@Module
class PhotoAlbumModule {
    private PhotoAlbumView mAlbumView;

    PhotoAlbumModule(PhotoAlbumView view) {
        this.mAlbumView = view;
    }

    @Provides
    PhotoAlbumView providePhotoAlbumView() {
        return mAlbumView;
    }
}
