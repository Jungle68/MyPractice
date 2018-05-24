package com.changsukuaidi.www.adapter.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.common.widget.recyclerview.base.ItemViewDelegate;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/20
 */

public class AlbumDetailDelegate implements ItemViewDelegate<PhotoBean> {
    private boolean showCheckBox; //是否显示图片CheckBox
    private int mImageSize;

    public AlbumDetailDelegate(Context context, int column, boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
        int dividerWidths = context.getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        mImageSize = (DisplayUtils.getWindowWidth(context) - dividerWidths * (column - 1)) / column;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_album_detail;
    }

    @Override
    public boolean isForViewType(PhotoBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, PhotoBean photoBean, int position) {
        holder.getConvertView().getLayoutParams().width = mImageSize;
        holder.getConvertView().getLayoutParams().height = mImageSize;
        if (showCheckBox) {
            holder.getConvertView().findViewById(R.id.v_selected).setVisibility(View.VISIBLE);
            if (photoBean.isSelect()) {
                holder.getConvertView().findViewById(R.id.v_selected).setSelected(true);
            } else {
                holder.getConvertView().findViewById(R.id.v_selected).setSelected(false);
            }
        } else {
            holder.getConvertView().findViewById(R.id.v_selected).setVisibility(View.GONE);
        }
        GlideApp.with(holder.getConvertView().getContext())
                .load(photoBean.getPath())
                .into((ImageView) holder.getConvertView().findViewById(R.id.iv_photo));

    }
}
