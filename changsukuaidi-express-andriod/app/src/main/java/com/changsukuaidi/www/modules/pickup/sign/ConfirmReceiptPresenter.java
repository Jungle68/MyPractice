package com.changsukuaidi.www.modules.pickup.sign;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.dagger.at.LifeScope;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */
@LifeScope
class ConfirmReceiptPresenter extends BasePresenter<ConfirmReceiptRepository, ConfirmReceiptView> {

    @Inject
    ConfirmReceiptPresenter(ConfirmReceiptView view, ConfirmReceiptRepository repository, BaseApplication application) {
        super(view, repository, application);
    }
}
