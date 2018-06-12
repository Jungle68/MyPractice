package com.changsukuaidi.www.modules.task.tripartite_dispatch;

import com.changsukuaidi.www.dagger.at.LifeScope;
import com.changsukuaidi.www.dagger.comp.AppComp;
import com.changsukuaidi.www.modules.task.list.TaskListFragment;
import com.changsukuaidi.www.modules.task.list.TaskListModule;

import dagger.Component;
import dagger.MembersInjector;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@LifeScope
@Component(dependencies = AppComp.class, modules = TripartiteModule.class)
public interface TripartiteComp extends MembersInjector<TripartiteFragment> {
}
