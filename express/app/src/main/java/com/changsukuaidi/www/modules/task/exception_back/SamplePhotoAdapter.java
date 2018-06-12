package com.changsukuaidi.www.modules.task.exception_back;

import android.content.Context;
import android.widget.ImageView;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/7
 * @Contact 605626708@qq.com
 */

public class SamplePhotoAdapter extends CommonAdapter<PhotoBean> {

    public SamplePhotoAdapter(Context context, List<PhotoBean> list) {
        super(context, R.layout.item_photo, list);
    }

    @Override
    protected void convert(ViewHolder holder, PhotoBean photoBean, int position) {

        ImageView img = holder.getView(R.id.img);
        img.getLayoutParams().width = (DisplayUtils.getWindowWidth(holder.getContext())
                - DisplayUtils.dp2px(holder.getContext(), 15) * 2
                - DisplayUtils.dp2px(holder.getContext(), 10) * 2) / 3;
        img.getLayoutParams().height = (DisplayUtils.getWindowWidth(holder.getContext())
                - DisplayUtils.dp2px(holder.getContext(), 15) * 2
                - DisplayUtils.dp2px(holder.getContext(), 10) * 2) / 3;

        if (photoBean == null) {
            holder.setImageResource(R.id.img, R.mipmap.ico_upload_center);
        } else {
            GlideApp.with(holder.getContext())
                    .load(photoBean.getPath())
                    .centerCrop()
                    .into(img);
        }
    }
}
