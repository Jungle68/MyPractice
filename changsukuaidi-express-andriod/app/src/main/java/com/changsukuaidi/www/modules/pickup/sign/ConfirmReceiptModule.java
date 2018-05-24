package com.changsukuaidi.www.modules.pickup.sign;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class ConfirmReceiptModule {
    private final ConfirmReceiptView mView;

    ConfirmReceiptModule(ConfirmReceiptView view) {
        mView = view;
    }

    @Provides
    ConfirmReceiptView provideLoginView(){
        return mView;
    }
}
