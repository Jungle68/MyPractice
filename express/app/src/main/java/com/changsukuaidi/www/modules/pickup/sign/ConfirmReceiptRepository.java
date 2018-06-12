package com.changsukuaidi.www.modules.pickup.sign;

import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.SettingsClient;

import javax.inject.Inject;


/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class ConfirmReceiptRepository {
    private SettingsClient mSettingsClient;

    @Inject
    ConfirmReceiptRepository(ClientManager clientManager) {
        mSettingsClient = clientManager.provideSettingsClient();
    }
}
