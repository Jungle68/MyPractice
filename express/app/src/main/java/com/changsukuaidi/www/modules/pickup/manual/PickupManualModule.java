package com.changsukuaidi.www.modules.pickup.manual;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class PickupManualModule {
    private final PickupManualView mView;

    PickupManualModule(PickupManualView view) {
        mView = view;
    }

    @Provides
    PickupManualView provideLoginView(){
        return mView;
    }
}
