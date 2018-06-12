package com.changsukuaidi.www.modules.colleague_pos;

import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.HomeClient;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

class ColleaguePosRepository {

    private HomeClient mHomeClient;

    @Inject
    ColleaguePosRepository(ClientManager clientManager) {
        mHomeClient = clientManager.provideHomeClient();
    }
}
