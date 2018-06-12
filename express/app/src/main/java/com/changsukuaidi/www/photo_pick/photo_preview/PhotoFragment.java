package com.changsukuaidi.www.photo_pick.photo_preview;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.i.IBasePresenter;
import com.changsukuaidi.www.databinding.FragmentPhotoGalleryBinding;
import com.changsukuaidi.www.databinding.PopupWindowDeleteImageBinding;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/1/4
 * @Contact 605626708@qq.com
 */

public class PhotoFragment extends BaseFragment<IBasePresenter, FragmentPhotoGalleryBinding> {

    protected PhotoAdapter mPhotoAdapter;   // 图片adapter
    protected List<String> photoList;       // 图片列表 - 待传
    protected int currentPosition;          // 图片当前选中位置 - 可传
    protected boolean needDelete;           // 是否需要删除按钮 - 可传
    private CommonPopupWindow mDeletePop;

    @Override
    protected int layoutId() {
        return R.layout.fragment_photo_gallery;
    }

    @Override
    protected void initIntent() {
        photoList = (List<String>) getActivity().getIntent().getSerializableExtra(Content.Extras.OBJECT);
        currentPosition = getActivity().getIntent().getIntExtra(Content.Extras.INTEGER, 0);
        needDelete = getActivity().getIntent().getBooleanExtra(Content.Extras.BOOLEAN, false);
    }

    @Override
    public boolean useTitle() {
        return false;
    }

    @Override
    public boolean showFullScreen() {
        return false;
    }

    @Override
    protected void initView() {
        mViewBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        mViewBinding.titleRight.setVisibility(needDelete ? View.VISIBLE : View.GONE);

        mViewBinding.photos.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                mViewBinding.title.setText(String.format(getString(R.string.photo_title_count), position + 1, photoList.size()));

            }
        });

        RxView.clicks(mViewBinding.titleRight)
                .compose(this.bindToLifecycle())
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mDeletePop == null) {
                        mDeletePop = new CommonPopupWindow.Builder(getActivity())
                                .setViewOnclickListener((view, layoutResId) -> {
                                    RxView.clicks(((PopupWindowDeleteImageBinding) view).tvCancel)
                                            .subscribe(aVoid1 -> mDeletePop.dismiss());
                                    RxView.clicks(((PopupWindowDeleteImageBinding) view).tvDelete)
                                            .throttleFirst(200, TimeUnit.MILLISECONDS)
                                            .subscribe(aVoid1 -> {
                                                mDeletePop.dismiss();
                                                photoList.remove(currentPosition);
                                                if (photoList.size() == currentPosition) {
                                                    currentPosition--;
                                                }
                                                if (photoList.size() > 0) {
                                                    mViewBinding.title.setText(String.format(getString(R.string.photo_title_count), currentPosition + 1, photoList.size()));
                                                    mPhotoAdapter.notifyDataSetChanged();
                                                } else {
                                                    getActivity().onBackPressed();
                                                }
                                            });
                                })
                                .setView(R.layout.popup_window_delete_image)
                                .create();
                    }
                    mDeletePop.setBackGroundLevel(0.5f);
                    mDeletePop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                });

    }

    @Override
    protected void initData() {
        mPhotoAdapter = new PhotoAdapter(photoList);
        mViewBinding.photos.setAdapter(mPhotoAdapter);
        mViewBinding.photos.setOffscreenPageLimit(5);

        mViewBinding.title.setText(String.format(getString(R.string.photo_title_count), currentPosition + 1, photoList.size()));
        mViewBinding.photos.setCurrentItem(currentPosition);
    }

    @Override
    protected boolean onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Content.Extras.OBJECT, (ArrayList) photoList);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        return true;
    }

    @Override
    protected void setPresenter() {

    }
}
