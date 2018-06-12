package com.changsukuaidi.www.widget.empty_layout;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/12/10
 * @Contact 605626708@qq.com
 */

public class EmptyLayoutBinding {

    @BindingAdapter("state")
    public static void setState(EmptyLayout emptyLayout, @EmptyLayout.EMPTY_STATE int state){
        emptyLayout.setState(state);
    }

    @BindingAdapter("onEmptyOrErrorClicked")
    public static void setOnEmptyClicked(EmptyLayout emptyLayout, View.OnClickListener onEmptyClicked){
        emptyLayout.setOnEmptyOrErrorClicked(onEmptyClicked);
    }
}
