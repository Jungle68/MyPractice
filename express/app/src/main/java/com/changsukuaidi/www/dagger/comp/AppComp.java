package com.changsukuaidi.www.dagger.comp;


import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.dagger.module.ClientModule;
import com.changsukuaidi.www.remote.ClientManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/12/21
 * @Contact 605626708@qq.com
 */
@Singleton
@Component(dependencies = BaseComp.class, modules = {ClientModule.class})
public interface AppComp{

    // 网络连接
    ClientManager provideClientManager();

    BaseApplication provideApp();
}
