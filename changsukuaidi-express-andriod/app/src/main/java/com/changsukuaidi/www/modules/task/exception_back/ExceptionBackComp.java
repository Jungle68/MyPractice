package com.changsukuaidi.www.modules.task.exception_back;

import com.changsukuaidi.www.dagger.at.LifeScope;
import com.changsukuaidi.www.dagger.comp.AppComp;

import dagger.Component;
import dagger.MembersInjector;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

@LifeScope
@Component(dependencies = AppComp.class, modules = ExceptionBackModule.class)
interface ExceptionBackComp extends MembersInjector<ExceptionBackFragment> {
}
