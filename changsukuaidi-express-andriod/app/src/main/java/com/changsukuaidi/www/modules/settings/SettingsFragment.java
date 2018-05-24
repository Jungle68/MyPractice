package com.changsukuaidi.www.modules.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Gravity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.databinding.FragmentSettingsBinding;
import com.changsukuaidi.www.databinding.PopupWindowPhotoPickBinding;
import com.changsukuaidi.www.modules.login.LoginActivity;
import com.changsukuaidi.www.modules.settings.modify_pwd.ModifyPwdActivity;
import com.changsukuaidi.www.photo_pick.CameraUtils;
import com.changsukuaidi.www.photo_pick.PhotoAlbumActivity;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.ToastUtils;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class SettingsFragment extends BaseFragment<SettingsPresenter, FragmentSettingsBinding> implements SettingsView, CommonPopupWindow.ViewInterface<PopupWindowPhotoPickBinding> {
    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_LOCAL_IMAGE = 102;
    private CommonPopupWindow mPhotoPickPop;
    private Uri contentUri;

    @Override
    protected void setPresenter() {
        DaggerSettingsComp.builder()
                .appComp(BaseApplication.getAppComp())
                .settingsModule(new SettingsModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initView() {
        super.initView();
        // set title
        setToolbarTitle(getString(R.string.setting));

        RxView.clicks(mViewBinding.btnLogout)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    // 清空token
                    ((BaseApplication) getActivity().getApplication()).clearTokenAndUser();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                });

        RxView.clicks(mViewBinding.llModifyPwd)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> startActivity(new Intent(getActivity(), ModifyPwdActivity.class)));

        RxView.clicks(mViewBinding.llUpdateCover)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> showPhotoPickPop());

        UserBean userBean = (UserBean) ACache.get(getActivity()).getAsObject(Content.USER);
        // 头像
        GlideApp.with(getContext())
                .load(userBean == null ? "" : userBean.getAvatar())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .apply(new RequestOptions().transform(new CircleCrop()))
                .into(mViewBinding.ivUserCover);
    }

    // 展示相册选择弹窗
    private void showPhotoPickPop() {
        if (mPhotoPickPop == null) {
            mPhotoPickPop = new CommonPopupWindow.Builder(getContext())
                    .setView(R.layout.popup_window_photo_pick)
                    .setViewOnclickListener(this)
                    .create();
        }
        mPhotoPickPop.setBackGroundLevel(0.7F);
        mPhotoPickPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void uploadCoverFinish(String avatar) {
        GlideApp.with(getContext())
                .load(avatar)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .apply(new RequestOptions().transform(new CircleCrop()))
                .into(mViewBinding.ivUserCover);

        UserBean userBean = (UserBean) ACache.get(getActivity()).getAsObject(Content.USER);
        if (userBean != null) {
            userBean.setAvatar(avatar);
            ACache.get(getActivity()).put(Content.USER, userBean);
        }
    }

    @Override
    protected boolean usePermissions() {
        return true;
    }

    @Override
    public void getChildViewBinding(PopupWindowPhotoPickBinding view, int layoutResId) {
        RxView.clicks(view.tvTakePhoto)
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        // DESC: 拍照
                        HashMap<String, Uri> result = CameraUtils.takePhoto(getActivity(), REQUEST_CAMERA);
                        for (String path : result.keySet()) {
                            contentUri = result.get(path);
                        }
                    }
                    mPhotoPickPop.dismiss();
                });

        RxView.clicks(view.tvFromAlbum)
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(granted -> {
                    if (granted)
                        startActivityForResult(new Intent(getActivity(), PhotoAlbumActivity.class), REQUEST_LOCAL_IMAGE);
                    mPhotoPickPop.dismiss();
                });

        RxView.clicks(view.tvCancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> mPhotoPickPop.dismiss());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    CameraUtils.cropPhotoByPath(getActivity(), contentUri, CameraUtils.UPDATE_HEAD);
                    break;
                case REQUEST_LOCAL_IMAGE:
                    if (data != null && data.hasExtra(Content.Extras.OBJECT)) {
                        ArrayList<PhotoBean> list = (ArrayList<PhotoBean>) data.getSerializableExtra(Content.Extras.OBJECT);
                        if (!list.isEmpty()) {
                            CameraUtils.cropPhotoByPath(getActivity(),
                                    Uri.fromFile(new File(list.get(0).getPath())),
                                    CameraUtils.UPDATE_HEAD);
                        }
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    Uri uri = UCrop.getOutput(data);
                    Bitmap bitmap = CameraUtils.getBitmapFromUri(getActivity(), uri);
                    mPresenter.uploadHeadIcon(bitmap);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            ToastUtils.showToast(getContext(), UCrop.getError(data).getMessage());
        }
    }
}
