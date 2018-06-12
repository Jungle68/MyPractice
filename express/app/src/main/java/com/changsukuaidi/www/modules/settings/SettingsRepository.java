package com.changsukuaidi.www.modules.settings;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.SettingsClient;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class SettingsRepository {
    private SettingsClient mSettingsClient;

    @Inject
    SettingsRepository(ClientManager clientManager) {
        mSettingsClient = clientManager.provideSettingsClient();
    }

    Observable<BaseJson<JsonObject>> uploadHeadIcon(String baseBitmap) {
        return mSettingsClient.uploadImage(baseBitmap);
    }

    Observable<BaseJson<JsonObject>> setUserHeadIcon(String img_id) {
        return mSettingsClient.setUserHeadIcon(img_id);
    }
}
