package com.changsukuaidi.www.photo_pick;

import com.changsukuaidi.www.dagger.at.LifeScope;
import com.changsukuaidi.www.dagger.comp.AppComp;

import dagger.Component;
import dagger.MembersInjector;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/2
 */
@LifeScope
@Component(dependencies = AppComp.class, modules = {PhotoAlbumDetailModule.class})
interface PhotoAlbumDetailComp extends MembersInjector<PhotoAlbumDetailFragment> {
}
