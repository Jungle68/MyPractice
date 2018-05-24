package com.changsukuaidi.www.photo_pick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.adapter.AlbumDetailAdapter;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.AlbumBean;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;
import com.changsukuaidi.www.databinding.FragmentAlbumPhotoDetailBinding;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.ToastUtils;
import com.changsukuaidi.www.utils.recycler_itemdecoration.GridDecoration;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class PhotoAlbumDetailFragment extends BaseFragment<PhotoAlbumDetailPresenter, FragmentAlbumPhotoDetailBinding> implements PhotoAlbumDetailView {

    public static final String ONLY_SELECT_ONE_PHOTO = "only_select_one_photo"; // 是否是单选模式
    public static final String CAN_SELECT_COUNT = "can_select_count"; // 还可以选择的张数
    public static final int MAX_SELECTED_COUNT = 9; // 最多可选择图片数
    public static final int PHOTO_PREVIEW = 1; // 预览相片

    private AlbumDetailAdapter mAlbumDetailAdapter; // 相片列表适配器

    private int mPosition; // 位于相册列表的位置
    private ArrayList<AlbumBean> mAlbumList; // 相册数据
    private ArrayList<PhotoBean> mSelectedPhotos; // 已选择的相片
    private ArrayList<PhotoBean> mPhotoList; // 需要显示的相片数据
    private int mCanChooseCount; // 还能选择的相片数

    public static PhotoAlbumDetailFragment newInstance(int position, ArrayList<AlbumBean> albumList, boolean isSingleMode, int count) {
        PhotoAlbumDetailFragment fragment = new PhotoAlbumDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PhotoAlbumFragment.SELECTED_DIRECTORY_NUMBER, position);
        bundle.putParcelableArrayList(PhotoAlbumFragment.ALL_PHOTOS, albumList);
        bundle.putBoolean(ONLY_SELECT_ONE_PHOTO, isSingleMode);
        bundle.putInt(CAN_SELECT_COUNT, count);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean mSinglePhotoSelect; // 单选模式

    @Override
    protected void setPresenter() {
        DaggerPhotoAlbumDetailComp.builder()
                .photoAlbumDetailModule(new PhotoAlbumDetailModule(this, mSinglePhotoSelect))
                .appComp(BaseApplication.getAppComp())
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_album_photo_detail;
    }

    @Override
    protected void initData() {
        mSelectedPhotos = new ArrayList<>();
    }

    @Override
    protected void initIntent() {
        mPosition = getArguments().getInt(PhotoAlbumFragment.SELECTED_DIRECTORY_NUMBER);
        mAlbumList = getArguments().getParcelableArrayList(PhotoAlbumFragment.ALL_PHOTOS);
        mSinglePhotoSelect = getArguments().getBoolean(ONLY_SELECT_ONE_PHOTO, true);
        mCanChooseCount = getArguments().getInt(CAN_SELECT_COUNT);
    }

    @Override
    protected void initView() {
        // 设置顶部Toolbar
        setToolbarTitle(mAlbumList.get(mPosition).getName());
        setToolbarRightText(getString(R.string.cancel), Color.parseColor("#FFFFFF"), view -> getActivity().finish());
        setToolbarLeftEnable();

        // 初始化ListView
        mPhotoList = (ArrayList<PhotoBean>) mAlbumList.get(mPosition).getPhotos();
        mAlbumDetailAdapter = new AlbumDetailAdapter(getActivity(), mPhotoList, 4, !mSinglePhotoSelect);
        mViewBinding.photoAlbumDetail.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mViewBinding.photoAlbumDetail.setAdapter(mAlbumDetailAdapter);
        mViewBinding.photoAlbumDetail.addItemDecoration(new GridDecoration(getActivity(), R.drawable.album_detail_divider));
        mAlbumDetailAdapter.notifyDataSetChanged();

        // 是否显示底部操作按钮
        if (mSinglePhotoSelect)
            mViewBinding.llOperate.setVisibility(View.GONE);

        // 点击事件
        RxView.clicks(mViewBinding.btComplete) // 完成
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(Content.Extras.OBJECT, mSelectedPhotos);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                });
        RxView.clicks(mViewBinding.tvPreview) // 预览
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> toPreviewActivity());

        // item点击事件
        mAlbumDetailAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                if (mSinglePhotoSelect) {
                    mSelectedPhotos.add(mPhotoList.get(position));
                    mViewBinding.btComplete.performClick();
                } else {
                    mPresenter.onSelectPhoto(mSelectedPhotos, mPhotoList.get(position), mCanChooseCount);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                return false;
            }
        });
    }

    // 跳转到预览界面
    private void toPreviewActivity() {
        ArrayList<String> photoPaths = new ArrayList<>();
        for (PhotoBean photo : mSelectedPhotos) {
            photoPaths.add(photo.getPath());
        }
//        Intent intent = new Intent(getActivity(), PhotoActivity.class);
//        intent.putStringArrayListExtra(Content.Extras.OBJECT, photoPaths);
//        intent.putExtra(Content.Extras.BOOLEAN, true);
//        startActivityForResult(intent, PHOTO_PREVIEW);
    }

    // 选择图片的视图更新
    @Override
    public void selectPhoto(boolean select, PhotoBean photo) {
        if (photo != null) {
            photo.setSelect(select);
            mAlbumDetailAdapter.notifyItemChanged(mPhotoList.indexOf(photo));
            int count = mSelectedPhotos.size();
            if (count > 0) {
                mViewBinding.tvPreview.setText(String.format(getString(R.string.preview_count), count));
                mViewBinding.tvPreview.setEnabled(true);
                mViewBinding.btComplete.setText(String.format(getString(R.string.complete_count), count, mCanChooseCount));
                mViewBinding.btComplete.setEnabled(true);
            } else {
                mViewBinding.tvPreview.setText(getString(R.string.preview));
                mViewBinding.tvPreview.setEnabled(false);
                mViewBinding.btComplete.setText(String.format(getString(R.string.complete_count), 0, mCanChooseCount));
                mViewBinding.btComplete.setEnabled(false);
            }
        } else {
            ToastUtils.showToast(getActivity(), String.format(getString(R.string.max_select), mCanChooseCount));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_PREVIEW && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ArrayList<String> photoList = data.getStringArrayListExtra(Content.Extras.OBJECT);
                Flowable.fromIterable(mSelectedPhotos)
                        .filter(photoBean -> !photoList.contains(photoBean.getPath()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(photoBean -> {
                            mSelectedPhotos.remove(photoBean);
                            selectPhoto(false, photoBean);
                        });
            }
        }
    }
}
