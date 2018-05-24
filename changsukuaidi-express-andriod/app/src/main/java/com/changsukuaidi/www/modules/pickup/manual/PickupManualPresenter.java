package com.changsukuaidi.www.modules.pickup.manual;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

class PickupManualPresenter extends BasePresenter<PickupManualRepository, PickupManualView> {

    @Inject
    PickupManualPresenter(PickupManualView view, PickupManualRepository repository, BaseApplication application) {
        super(view, repository, application);
    }
}
