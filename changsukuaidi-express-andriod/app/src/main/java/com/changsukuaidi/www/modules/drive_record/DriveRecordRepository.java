package com.changsukuaidi.www.modules.drive_record;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.DriveRecordBean;
import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.DriveRecordClient;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/2
 */

class DriveRecordRepository {
    private DriveRecordClient mDriveRecordClient;

    @Inject
    DriveRecordRepository(ClientManager clientManager) {
        mDriveRecordClient = clientManager.provideDriveRecordClient();
    }

    Observable<BaseJson<List<DriveRecordBean>>> getRecordList(int page, String time) {
        return mDriveRecordClient.getRecordList(page, time)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
