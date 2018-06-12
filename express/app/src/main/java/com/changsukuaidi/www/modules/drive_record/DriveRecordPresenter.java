package com.changsukuaidi.www.modules.drive_record;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseListPresenter;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.bean.DriveRecordBean;
import com.changsukuaidi.www.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class DriveRecordPresenter extends BaseListPresenter<DriveRecordRepository, DriveRecordViewList> {

    @Inject
    DriveRecordPresenter(DriveRecordViewList view, DriveRecordRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    @Override
    public void requestListData(boolean isLoadMore, int page) {
        getRecordList(page, mRootView.getTime(), isLoadMore);
    }

    void getRecordList(int page, String time, boolean isLoadMore) {
        mRepository.getRecordList(page, time)
                .subscribe(new BaseObserver<List<DriveRecordBean>>() {
                    @Override
                    public void onSuccess(String message, List<DriveRecordBean> driveRecordBeans) {
                        mRootView.loadDataSuccess(isLoadMore, driveRecordBeans);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        ToastUtils.showToast(message);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }
}
