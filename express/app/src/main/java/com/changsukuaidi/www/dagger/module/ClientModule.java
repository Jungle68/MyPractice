package com.changsukuaidi.www.dagger.module;


import com.changsukuaidi.www.remote.DriveRecordClient;
import com.changsukuaidi.www.remote.HomeClient;
import com.changsukuaidi.www.remote.LoginClient;
import com.changsukuaidi.www.remote.PunchClient;
import com.changsukuaidi.www.remote.SettingsClient;
import com.changsukuaidi.www.remote.TaskClient;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/12/21
 * @Contact 605626708@qq.com
 */

@Module
public class ClientModule {

    @Provides
    public static LoginClient provideLoginClient(Retrofit retrofit) {
        return retrofit.create(LoginClient.class);
    }

    @Provides
    public static PunchClient providePunchClient(Retrofit retrofit) {
        return retrofit.create(PunchClient.class);
    }

    @Provides
    public static HomeClient provideHomeClient(Retrofit retrofit) {
        return retrofit.create(HomeClient.class);
    }

    @Provides
    public static SettingsClient provideSettingsClient(Retrofit retrofit) {
        return retrofit.create(SettingsClient.class);
    }

    @Provides
    public static TaskClient provideTaskClient(Retrofit retrofit) {
        return retrofit.create(TaskClient.class);
    }

    @Provides
    public static DriveRecordClient provideDriveRecord(Retrofit retrofit) {
        return retrofit.create(DriveRecordClient.class);
    }
}
