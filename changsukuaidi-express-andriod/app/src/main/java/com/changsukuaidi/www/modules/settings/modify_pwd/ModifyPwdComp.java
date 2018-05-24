package com.changsukuaidi.www.modules.settings.modify_pwd;

import com.changsukuaidi.www.dagger.at.LifeScope;
import com.changsukuaidi.www.dagger.comp.AppComp;
import com.changsukuaidi.www.modules.settings.SettingsFragment;
import com.changsukuaidi.www.modules.settings.SettingsModule;

import dagger.Component;
import dagger.MembersInjector;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@LifeScope
@Component(dependencies = AppComp.class, modules = ModifyPwdModule.class)
interface ModifyPwdComp extends MembersInjector<ModifyPwdFragment> {
}
