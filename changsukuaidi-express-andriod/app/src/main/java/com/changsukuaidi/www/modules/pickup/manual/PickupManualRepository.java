package com.changsukuaidi.www.modules.pickup.manual;

import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.SettingsClient;

import javax.inject.Inject;


/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class PickupManualRepository {
    private SettingsClient mSettingsClient;

    @Inject
    PickupManualRepository(ClientManager clientManager) {
        mSettingsClient = clientManager.provideSettingsClient();
    }
}
