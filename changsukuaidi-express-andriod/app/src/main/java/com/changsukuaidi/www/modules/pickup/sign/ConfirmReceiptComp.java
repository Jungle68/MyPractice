package com.changsukuaidi.www.modules.pickup.sign;

import com.changsukuaidi.www.dagger.at.LifeScope;
import com.changsukuaidi.www.dagger.comp.AppComp;

import dagger.Component;
import dagger.MembersInjector;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@LifeScope
@Component(dependencies = AppComp.class, modules = ConfirmReceiptModule.class)
interface ConfirmReceiptComp extends MembersInjector<ConfirmReceiptFragment> {
}
