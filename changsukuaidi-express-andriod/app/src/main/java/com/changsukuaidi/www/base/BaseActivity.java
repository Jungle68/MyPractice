package com.changsukuaidi.www.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.utils.ActivityUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Stack;


/**
 * @Describe
 * @Author zhiyicx
 * @Date 2017/11/30
 * @Contact
 */

public abstract class BaseActivity<F extends BaseFragment> extends AppCompatActivity {

    protected F mContainFragment;
    private static Stack<BaseActivity> mActivityStack = new Stack<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);//轨迹查询监听器
        initIntent();
        initView();
        initData();
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        mActivityStack.push(this);
    }

    protected void initIntent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().unregister(this);
        mActivityStack.remove(this);
    }

    protected boolean useEventBus() {
        return false;
    }

    protected void initView() {
        if (mContainFragment == null) {
            mContainFragment = getFragment();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mContainFragment, R.id.fl_fragment_container);
    }

    protected abstract F getFragment();

    protected abstract void initData();

    protected int layoutId() {
        return R.layout.activity_base;
    }

    @Override
    public void onBackPressed() {
        if (!mContainFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }


    public static Stack<BaseActivity> getmActivityStack() {
        return mActivityStack;
    }

    public static BaseActivity getLastActivity() {
        return mActivityStack.peek();
    }

    public static void clearTaskStayOne(BaseActivity stayActivity) {
        if (stayActivity != null) {
            mActivityStack.remove(stayActivity);
            while (mActivityStack.elements().hasMoreElements()) {
                BaseActivity activity= mActivityStack.elements().nextElement();
                activity.finish();
                mActivityStack.remove(activity);
            }
            mActivityStack.add(stayActivity);
        }
    }
}
