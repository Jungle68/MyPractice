package com.changsukuaidi.www.modules.task.exception_back;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;
import com.changsukuaidi.www.databinding.FragmentExceptionBackBinding;
import com.changsukuaidi.www.databinding.PopupWindowExceptionTypeBinding;
import com.changsukuaidi.www.databinding.PopupWindowPhotoPickBinding;
import com.changsukuaidi.www.photo_pick.CameraUtils;
import com.changsukuaidi.www.photo_pick.PhotoAlbumActivity;
import com.changsukuaidi.www.photo_pick.photo_preview.PhotoActivity;
import com.changsukuaidi.www.utils.ActivityUtils;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.recycler_itemdecoration.LinearDecoration;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.changsukuaidi.www.modules.settings.SettingsFragment.REQUEST_CAMERA;
import static com.changsukuaidi.www.modules.settings.SettingsFragment.REQUEST_LOCAL_IMAGE;
import static com.changsukuaidi.www.photo_pick.PhotoAlbumDetailFragment.CAN_SELECT_COUNT;
import static com.changsukuaidi.www.photo_pick.PhotoAlbumDetailFragment.ONLY_SELECT_ONE_PHOTO;
import static com.changsukuaidi.www.photo_pick.PhotoAlbumDetailFragment.PHOTO_PREVIEW;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/6
 * @Contact 605626708@qq.com
 */

public class ExceptionBackFragment extends BaseFragment<ExceptionBackPresenter, FragmentExceptionBackBinding> implements IExceptionBackView, CommonPopupWindow.ViewInterface {
    private int mTaskType = 0; //0、配送 1、中转和物料配送
    protected SamplePhotoAdapter mSamplePhotoAdapter;
    // 图片数据源
    protected List<PhotoBean> mListDatas = new ArrayList<>();

    private CommonPopupWindow mPhotoPickPop; // 选择图片的弹窗
    private CommonPopupWindow mExceptionType; // 选择异常种类的弹窗
    private ArrayList<String> mTypes; // 异常种类

    @Override
    protected void setPresenter() {
        DaggerExceptionBackComp.builder()
                .appComp(BaseApplication.getAppComp())
                .exceptionBackModule(new ExceptionBackModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initIntent() {
        mTaskType = getActivity().getIntent().getIntExtra("task_type", 0);
        mTypes = new ArrayList<>();
        if (mTaskType == 0) {
            mTypes.add("联系不上");
            mTypes.add("地址更改");
            mTypes.add("延期送达");
            mTypes.add("其他");
        } else {
            mTypes.add("车辆故障");
            mTypes.add("其他");
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_exception_back;
    }

    @Override
    protected void initView() {
        super.initView();

        // set title
        setToolbarTitle(getString(R.string.exception_apply));
        setToolbarRight(getString(R.string.post), v -> {
            // TODO: 2018/3/6
            // NAME: zhouhao
            // DESC: 提交异常
        });

        mListDatas.add(null);
        mSamplePhotoAdapter = new SamplePhotoAdapter(getContext(), mListDatas);
        mViewBinding.exceptionCertificate.setAdapter(mSamplePhotoAdapter);
        mViewBinding.exceptionCertificate.setNestedScrollingEnabled(false);
        mViewBinding.exceptionCertificate.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mViewBinding.exceptionCertificate.addItemDecoration(new LinearDecoration(
                DisplayUtils.dp2px(getContext(), 5),
                DisplayUtils.dp2px(getContext(), 5),
                DisplayUtils.dp2px(getContext(), 5),
                DisplayUtils.dp2px(getContext(), 5)
        ));

        mSamplePhotoAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                // 添加图片
                if (mListDatas.get(position) == null) {
                    if (mPhotoPickPop == null)
                        DisplayUtils.hideSoftInput(getActivity(), view.getWindowToken());
                    mPhotoPickPop = new CommonPopupWindow.Builder(getContext())
                            .setView(R.layout.popup_window_photo_pick)
                            .setViewOnclickListener(ExceptionBackFragment.this)
                            .create();
                    ActivityUtils.setWindowBackgroundAlpha(getContext(), 0.5f);
                    mPhotoPickPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                }
                // 查看已添加的图片
                else {
                    ArrayList<String> photoList = new ArrayList<>();
                    if (mListDatas.get(mListDatas.size() - 1) == null)
                        mListDatas.remove(mListDatas.size() - 1);
                    Observable.fromIterable(mListDatas)
                            .filter(baseBean1 -> baseBean1.getPath() != null)
                            .map(PhotoBean::getPath)
                            .toList()
                            .subscribe(list -> {
                                photoList.addAll(list);
                                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                                intent.putStringArrayListExtra(Content.Extras.OBJECT, photoList);
                                intent.putExtra(Content.Extras.BOOLEAN, true);
                                intent.putExtra(Content.Extras.INTEGER, position);
                                startActivityForResult(intent, PHOTO_PREVIEW);
                            });
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                return false;
            }
        });

        RxView.clicks(mViewBinding.llExceptionType)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    // 打开选择异常类型的弹窗
                    openExceptionTypesPop();
                });
    }

    @Override
    public void getChildViewBinding(ViewDataBinding view, int layoutResId) {
        if (layoutResId == R.layout.popup_window_photo_pick) {
            PopupWindowPhotoPickBinding binding = (PopupWindowPhotoPickBinding) view;
            RxView.clicks(binding.tvTakePhoto)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA))
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            // DESC: 拍照
                            mViewBinding.exceptionCertificate.setTag(CameraUtils.takePhoto(getActivity(), REQUEST_CAMERA));
                        }
                        mPhotoPickPop.dismiss();
                    });

            RxView.clicks(binding.tvFromAlbum)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE))
                    .subscribe(granted -> {
                        if (granted)
                            startActivityForResult(new Intent(getActivity(), PhotoAlbumActivity.class)
                                    .putExtra(ONLY_SELECT_ONE_PHOTO, false)
                                    .putExtra(CAN_SELECT_COUNT, 9 - mListDatas.size() + 1), REQUEST_LOCAL_IMAGE);
                        mPhotoPickPop.dismiss();
                    });

            RxView.clicks(binding.tvCancel)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(o -> mPhotoPickPop.dismiss());
        } else if (layoutResId == R.layout.popup_window_exception_type) {
            PopupWindowExceptionTypeBinding binding = (PopupWindowExceptionTypeBinding) view;
            binding.rvExceptionTypes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            binding.rvExceptionTypes.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.popup_window_exception_type_item, mTypes) {
                @Override
                protected void convert(ViewHolder holder, String s, int position) {
                    holder.setText(R.id.tv_type, s);
                    holder.setOnClickListener(R.id.tv_type, view1 -> {
                        mViewBinding.exceptionType.setText(s);
                        mExceptionType.dismiss();
                        if (TextUtils.equals(s, "其他")) {
                            mViewBinding.exceptionReason.setVisibility(View.VISIBLE);
                        } else {
                            mViewBinding.exceptionReason.setVisibility(View.GONE);
                        }
                    });
                }
            });
            RxView.clicks(binding.tvCancel)
                    .subscribe(o -> mExceptionType.dismiss());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    HashMap<String, Uri> result = (HashMap<String, Uri>) mViewBinding.exceptionCertificate.getTag();
                    if (result != null) {
                        mListDatas.remove(mListDatas.size() - 1);
                        for (String path : result.keySet()) {
                            mListDatas.add(new PhotoBean(path));
                        }
                        if (mListDatas.size() < 9)
                            mListDatas.add(null);
                        mSamplePhotoAdapter.notifyDataSetChanged();
                        mPhotoPickPop.dismiss();
                    }
                    break;
                case REQUEST_LOCAL_IMAGE:
                    if (data != null) {
                        mListDatas.remove(mListDatas.size() - 1);
                        mListDatas.addAll(data.getParcelableArrayListExtra(Content.Extras.OBJECT));
                        if (mListDatas.size() < 9) {
                            mListDatas.add(null);
                        }
                        mSamplePhotoAdapter.notifyDataSetChanged();
                        mPhotoPickPop.dismiss();
                    }
                    break;
                case PHOTO_PREVIEW:
                    if (data != null) {
                        ArrayList<String> photoList = data.getStringArrayListExtra(Content.Extras.OBJECT);
                        Observable.fromIterable(mListDatas)
                                .filter(photoBean -> photoBean != null && photoList.contains(photoBean.getPath()))
                                .toList()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(photoBean -> {
                                    mListDatas.clear();
                                    mListDatas.addAll(photoBean);
                                    if (mListDatas.size() < 9)
                                        mListDatas.add(null);
                                    mSamplePhotoAdapter.notifyDataSetChanged();
                                });
                    }
                    break;
            }
        }
    }

    @Override
    protected boolean usePermissions() {
        return true;
    }

    /**
     * 打开选择异常类型弹窗
     */
    private void openExceptionTypesPop() {
        DisplayUtils.hideSoftInput(getActivity(), mViewBinding.llExceptionType.getWindowToken());
        if (mExceptionType == null) {
            mExceptionType = new CommonPopupWindow.Builder(getContext())
                    .setView(R.layout.popup_window_exception_type)
                    .setViewOnclickListener(this)
                    .setWidthAndHeight(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    .create();
        }
        mExceptionType.setBackGroundLevel(0.5f);
        mExceptionType.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
}
