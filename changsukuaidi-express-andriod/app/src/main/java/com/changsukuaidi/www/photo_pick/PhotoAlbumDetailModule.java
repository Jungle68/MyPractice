package com.changsukuaidi.www.photo_pick;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/2
 */
@Module
class PhotoAlbumDetailModule {
    private PhotoAlbumDetailView mAlbumView;
    private boolean isSingleMode;

    PhotoAlbumDetailModule(PhotoAlbumDetailView view, boolean isSingleMode) {
        this.mAlbumView = view;
        this.isSingleMode = isSingleMode;
    }

    @Provides
    PhotoAlbumDetailView providePhotoAlbumView() {
        return mAlbumView;
    }

    @Provides
    boolean providerIsSingleMode() {
        return isSingleMode;
    }
}
