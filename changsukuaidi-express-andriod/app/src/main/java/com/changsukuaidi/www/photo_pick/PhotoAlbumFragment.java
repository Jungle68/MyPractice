package com.changsukuaidi.www.photo_pick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.adapter.AlbumDirAdapter;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.AlbumBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;
import com.changsukuaidi.www.databinding.FragmentAlbumPhotoBinding;

import java.util.ArrayList;


/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class PhotoAlbumFragment extends BaseFragment<PhotoAlbumPresenter, FragmentAlbumPhotoBinding> implements PhotoAlbumView {
    // 相册列表被选中的位置
    public static final String SELECTED_DIRECTORY_NUMBER = "selected_directory_number";
    // 相册列表的内容
    public static final String ALL_PHOTOS = "all_photos";
    public static final int ALBUM_DETAIL = 1;  // 查看相册详情

    private AlbumDirAdapter mAlbumDirAdapter; // 相册列表适配器
    private ArrayList<AlbumBean> mAlbumList; // 相册文件集合
    private boolean mIsSingleMode = true; // 是否是单选一张照片的模式
    private int mCanChooseCount; // 还能选择的相片数

    public static PhotoAlbumFragment newInstance(boolean isSingleMode, int count) {
        PhotoAlbumFragment fragment = new PhotoAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PhotoAlbumDetailFragment.ONLY_SELECT_ONE_PHOTO, isSingleMode);
        bundle.putInt(PhotoAlbumDetailFragment.CAN_SELECT_COUNT, count);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setPresenter() {
        DaggerPhotoAlbumComp.builder()
                .appComp(BaseApplication.getAppComp())
                .photoAlbumModule(new PhotoAlbumModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_album_photo;
    }

    @Override
    protected void initData() {
        mPresenter.loadAlbumDatas(getActivity());
        mIsSingleMode = getArguments().getBoolean(PhotoAlbumDetailFragment.ONLY_SELECT_ONE_PHOTO);
        mCanChooseCount = getArguments().getInt(PhotoAlbumDetailFragment.CAN_SELECT_COUNT);
    }

    @Override
    protected void initView() {
        // 设置顶部Toolbar
        setToolbarTitle(getString(R.string.setting));

        // initView
        mAlbumList = new ArrayList<>();
        mAlbumDirAdapter = new AlbumDirAdapter(getActivity(), mAlbumList);
        mViewBinding.photoAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mViewBinding.photoAlbum.setAdapter(mAlbumDirAdapter);

        mAlbumDirAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(SELECTED_DIRECTORY_NUMBER, position);
                bundle.putParcelableArrayList(ALL_PHOTOS, mAlbumList);
                bundle.putBoolean(PhotoAlbumDetailFragment.ONLY_SELECT_ONE_PHOTO, mIsSingleMode);
                bundle.putInt(PhotoAlbumDetailFragment.CAN_SELECT_COUNT, mCanChooseCount);
                Intent intent = new Intent(getActivity(), PhotoAlbumDetailActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ALBUM_DETAIL);
                // 从右边进入，从左边出去
                getActivity().overridePendingTransition(R.anim.slide_from_right_in, R.anim.slide_from_left_out);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                return false;
            }
        });
    }

    @Override
    public void finishLoadAlbumDatas(ArrayList<AlbumBean> albumBeanList) {
        mAlbumList.clear();
        mAlbumList.addAll(albumBeanList);
        mAlbumDirAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM_DETAIL && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }
}
