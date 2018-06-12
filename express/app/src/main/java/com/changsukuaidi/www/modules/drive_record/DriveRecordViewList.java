package com.changsukuaidi.www.modules.drive_record;

import com.changsukuaidi.www.base.i.IBaseListView;
import com.changsukuaidi.www.bean.DriveRecordBean;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/2
 */

interface DriveRecordViewList extends IBaseListView<DriveRecordBean> {
    String getTime();
}
