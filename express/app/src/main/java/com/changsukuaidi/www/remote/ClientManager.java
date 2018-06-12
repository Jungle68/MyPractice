package com.changsukuaidi.www.remote;

import javax.inject.Inject;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/12/21
 * @Contact 605626708@qq.com
 */

public class ClientManager {

    private LoginClient mLoginClient;
    private PunchClient mPunchClient;
    private HomeClient mHomeClient;
    private SettingsClient mSettingsClient;
    private TaskClient mTaskClient;
    private DriveRecordClient mDriveRecordClient;

    @Inject
    public ClientManager(LoginClient loginClient,
                         PunchClient punchClient,
                         HomeClient homeClient,
                         SettingsClient settingsClient,
                         TaskClient taskClient,
                         DriveRecordClient driveRecordClient) {
        mLoginClient = loginClient;
        mPunchClient = punchClient;
        mHomeClient = homeClient;
        mSettingsClient = settingsClient;
        mTaskClient = taskClient;
        mDriveRecordClient = driveRecordClient;
    }

    public LoginClient provideLoginClient() {
        return mLoginClient;
    }

    public PunchClient providePunchClient() {
        return mPunchClient;
    }

    public HomeClient provideHomeClient() {
        return mHomeClient;
    }

    public SettingsClient provideSettingsClient() {
        return mSettingsClient;
    }

    public TaskClient provideTaskClient() {
        return mTaskClient;
    }

    public DriveRecordClient provideDriveRecordClient() {
        return mDriveRecordClient;
    }
}
