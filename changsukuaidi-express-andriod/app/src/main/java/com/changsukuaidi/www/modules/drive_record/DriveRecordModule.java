package com.changsukuaidi.www.modules.drive_record;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/7
 * @Contact 605626708@qq.com
 */

@Module
class DriveRecordModule {
    private final DriveRecordViewList mView;

    DriveRecordModule(DriveRecordViewList view) {
        mView = view;
    }

    @Provides
    DriveRecordViewList provideLoginView(){
        return mView;
    }
}
