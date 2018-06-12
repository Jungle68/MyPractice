package com.changsukuaidi.www.modules.pickup.sign;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;
import com.changsukuaidi.www.databinding.FragmentConfirmReceiptBinding;
import com.changsukuaidi.www.databinding.PopupWindowPhotoPickBinding;
import com.changsukuaidi.www.photo_pick.CameraUtils;
import com.changsukuaidi.www.photo_pick.PhotoAlbumActivity;
import com.changsukuaidi.www.photo_pick.PhotoAlbumDetailFragment;
import com.changsukuaidi.www.photo_pick.photo_preview.PhotoActivity;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class ConfirmReceiptFragment extends BaseFragment<ConfirmReceiptPresenter, FragmentConfirmReceiptBinding> implements ConfirmReceiptView, CommonPopupWindow.ViewInterface<PopupWindowPhotoPickBinding> {
    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_LOCAL_IMAGE = 102;
    public static final int REQUEST_SEE_IMAGE = 104;
    public static final int MAX_SELECT_COUNT = 6;
    private ArrayList<PhotoBean> mCertificateImgs;
    private ArrayList<String> mImgPaths;
    private CommonPopupWindow mPhotoPickPop;
    private String mCameraPath;
    private int mImgWidth = 0;

    @Override
    protected void setPresenter() {
        DaggerConfirmReceiptComp.builder()
                .appComp(BaseApplication.getAppComp())
                .confirmReceiptModule(new ConfirmReceiptModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initView() {
        setToolbarTitle(getString(R.string.confirm));

        mImgWidth = (DisplayUtils.getWindowWidth(getContext()) - DisplayUtils.dp2px(getContext(), 60)) / 3;
        mCertificateImgs = new ArrayList<>();
        mImgPaths = new ArrayList<>();
        mCertificateImgs.add(new PhotoBean(""));
        mViewBinding.rvCertificateImgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mViewBinding.rvCertificateImgs.setAdapter(new CommonAdapter<PhotoBean>(getContext(), R.layout.delegate_certificate_img_item, mCertificateImgs) {
            @Override
            protected void convert(ViewHolder holder, PhotoBean photoBean, int position) {
                initRvItem(holder, photoBean, position);
            }
        });
    }

    @Override
    protected boolean usePermissions() {
        return true;
    }

    // 绑定RecyclerView的数据
    private void initRvItem(ViewHolder holder, PhotoBean photoBean, int position) {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(mImgWidth, mImgWidth);
        layoutParams.bottomMargin = DisplayUtils.dp2px(getContext(), 7.5F);
        layoutParams.topMargin = DisplayUtils.dp2px(getContext(), 7.5F);
        layoutParams.leftMargin = DisplayUtils.dp2px(getContext(), 7.5F);
        layoutParams.rightMargin = DisplayUtils.dp2px(getContext(), 7.5F);
        holder.getView(R.id.iv_certificate_img).setLayoutParams(layoutParams);
        if (TextUtils.isEmpty(photoBean.getPath())) {
            holder.setImageResource(R.id.iv_certificate_img, R.mipmap.icon_add);
        } else {
            GlideApp.with(getContext())
                    .load(photoBean.getPath())
                    .error(new ColorDrawable(getResources().getColor(R.color.themeColor)))
                    .placeholder(new ColorDrawable(getResources().getColor(R.color.themeColor)))
                    .into((ImageView) holder.getView(R.id.iv_certificate_img));
        }
        RxView.clicks(holder.getView(R.id.iv_certificate_img))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (TextUtils.isEmpty(photoBean.getPath())) {
                        showPhotoPickPop(holder.getView(R.id.iv_certificate_img));
                    } else {
                        mImgPaths.clear();
                        Flowable.fromIterable(mCertificateImgs)
                                .filter(photo -> !TextUtils.isEmpty(photo.getPath()))
                                .map(photo -> photo.getPath())
                                .toList()
                                .subscribe(photos -> {
                                    mImgPaths.addAll(photos);
                                    Intent intent = new Intent(getActivity(), PhotoActivity.class);
                                    intent.putExtra(Content.Extras.OBJECT, mImgPaths);
                                    intent.putExtra(Content.Extras.INTEGER, position);
                                    intent.putExtra(Content.Extras.BOOLEAN, true);
                                    startActivityForResult(intent, REQUEST_SEE_IMAGE);
                                });

                    }
                });
    }

    // 展示相册选择弹窗
    private void showPhotoPickPop(View view) {
        if (mPhotoPickPop == null) {
            DisplayUtils.hideSoftInput(getActivity(), view.getWindowToken());
            mPhotoPickPop = new CommonPopupWindow.Builder(getContext())
                    .setView(R.layout.popup_window_photo_pick)
                    .setViewOnclickListener(this)
                    .create();
        }
        mPhotoPickPop.setBackGroundLevel(0.7F);
        mPhotoPickPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_confirm_receipt;
    }

    @Override
    public void getChildViewBinding(PopupWindowPhotoPickBinding view, int layoutResId) {
        // 拍照
        RxView.clicks(view.tvTakePhoto)
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        HashMap<String, Uri> result = CameraUtils.takePhoto(getActivity(), REQUEST_CAMERA);
                        for (String path : result.keySet()) {
                            mCameraPath = path;
                        }
                    }
                    mPhotoPickPop.dismiss();
                });

        // 选择图片
        RxView.clicks(view.tvFromAlbum)
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(getActivity(), PhotoAlbumActivity.class);
                        intent.putExtra(PhotoAlbumDetailFragment.CAN_SELECT_COUNT, MAX_SELECT_COUNT - mCertificateImgs.size() + 1);
                        intent.putExtra(PhotoAlbumDetailFragment.ONLY_SELECT_ONE_PHOTO, false);
                        startActivityForResult(intent, REQUEST_LOCAL_IMAGE);
                    }
                    mPhotoPickPop.dismiss();
                });

        // 取消
        RxView.clicks(view.tvCancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> mPhotoPickPop.dismiss());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    Flowable.just(mCameraPath)
                            .filter(s -> !TextUtils.isEmpty(s))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                mCertificateImgs.remove(mCertificateImgs.size() - 1);
                                mCertificateImgs.add(new PhotoBean(s));
                                if (mCertificateImgs.size() < MAX_SELECT_COUNT)
                                    mCertificateImgs.add(new PhotoBean(""));
                                mViewBinding.rvCertificateImgs.getAdapter().notifyDataSetChanged();
                                mCameraPath = "";
                            });
                    break;
                case REQUEST_LOCAL_IMAGE:
                    Flowable.just(data)
                            .map(intent -> (ArrayList<PhotoBean>) intent.getSerializableExtra(Content.Extras.OBJECT))
                            .filter(photoBeans -> !photoBeans.isEmpty())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(list -> {
                                mCertificateImgs.remove(mCertificateImgs.size() - 1);
                                mCertificateImgs.addAll(list);
                                if (mCertificateImgs.size() < MAX_SELECT_COUNT)
                                    mCertificateImgs.add(new PhotoBean(""));
                                mViewBinding.rvCertificateImgs.getAdapter().notifyDataSetChanged();
                            });
                    break;
                case REQUEST_SEE_IMAGE:
                    Flowable.just(data)
                            .flatMap(intent -> {
                                ArrayList<String> list = (ArrayList<String>) intent.getSerializableExtra(Content.Extras.OBJECT);
                                mImgPaths.clear();
                                mImgPaths.addAll(list);
                                return Flowable.fromIterable(list);
                            })
                            .map(s -> new PhotoBean(s))
                            .toList()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(list -> {
                                mCertificateImgs.clear();
                                mCertificateImgs.addAll(list);
                                if (mCertificateImgs.size() < MAX_SELECT_COUNT)
                                    mCertificateImgs.add(new PhotoBean(""));
                                mViewBinding.rvCertificateImgs.getAdapter().notifyDataSetChanged();
                            });
                    break;
            }
        }
    }
}
