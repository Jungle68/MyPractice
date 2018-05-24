package com.changsukuaidi.www.modules.colleague_pos;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/8
 * @Contact 605626708@qq.com
 */

class ColleaguePresenter extends BasePresenter<ColleaguePosRepository, IColleaguePosView> {
    @Inject
    ColleaguePresenter(IColleaguePosView view, ColleaguePosRepository repository, BaseApplication application) {
        super(view, repository, application);
    }
}
