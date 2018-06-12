package com.changsukuaidi.www.modules.task.tripartite_dispatch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;
import com.changsukuaidi.www.databinding.FragmentTripartiteBinding;
import com.changsukuaidi.www.databinding.PopupWindowPhotoPickBinding;
import com.changsukuaidi.www.modules.task.exception_back.SamplePhotoAdapter;
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
 * @Date 2018/3/7
 * @Contact 605626708@qq.com
 */

public class TripartiteFragment extends BaseFragment<TripartitePresenter, FragmentTripartiteBinding> implements ITripartiteView, CommonPopupWindow.ViewInterface<PopupWindowPhotoPickBinding> {

    protected SamplePhotoAdapter mSamplePhotoAdapter;

    // 图片数据源
    protected List<PhotoBean> mListDatas = new ArrayList<>();

    private CommonPopupWindow mPhotoPickPop; // 选择图片
    private PopupWindow mExpressCate; // 快递公司弹窗
    private ArrayList<String> mExpressList; // 选择快递公司列表

    @Override
    protected void setPresenter() {
        DaggerTripartiteComp.builder()
                .appComp(BaseApplication.getAppComp())
                .tripartiteModule(new TripartiteModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_tripartite;
    }

    @Override
    protected void initData() {
        mExpressList = new ArrayList<>();
        mExpressList.add("顺丰");
        mExpressList.add("中通");
        mExpressList.add("申通");
        mExpressList.add("EMS");
        mExpressList.add("韵达");
        mExpressList.add("天天");
    }

    @Override
    protected void initView() {
        super.initView();
        // set title
        setToolbarTitle(getString(R.string.tripartite_dispatch));
        setToolbarRight(getString(R.string.confirm), v -> {
            // TODO: 2018/3/6
            // NAME: zhouhao
            // DESC: 提交异常
        });

        // 选择三方配送快递品牌
        RxView.clicks(mViewBinding.expressBrand)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mExpressCate == null) {
                        mExpressCate = new PopupWindow();
                        RecyclerView tripartiteCate = (RecyclerView) getLayoutInflater().inflate(R.layout.popup_window_tripartite_cate, mViewBinding.postOrderCertificate, false);
                        tripartiteCate.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        tripartiteCate.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.tripart_express_item, mExpressList) {
                            @Override
                            protected void convert(ViewHolder holder, String s, int position) {
                                holder.setText(R.id.tv_express_cate, s);
                                holder.setOnClickListener(R.id.tv_express_cate, view -> {
                                    mViewBinding.expressBrand.setText(s);
                                    mExpressCate.dismiss();
                                });
                            }
                        });
                        mExpressCate.setContentView(tripartiteCate);
                        mExpressCate.setWidth(mViewBinding.expressBrand.getWidth());
                        mExpressCate.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mExpressCate.setOutsideTouchable(true);
                    }
                    mExpressCate.showAsDropDown(mViewBinding.expressBrand);
                });

        mListDatas.add(null);
        mSamplePhotoAdapter = new SamplePhotoAdapter(getContext(), mListDatas);
        mViewBinding.postOrderCertificate.setNestedScrollingEnabled(false);
        mViewBinding.postOrderCertificate.setAdapter(mSamplePhotoAdapter);
        mViewBinding.postOrderCertificate.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mViewBinding.postOrderCertificate.addItemDecoration(new LinearDecoration(
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
                    DisplayUtils.hideSoftInput(getActivity(), view.getWindowToken());
                    if (mPhotoPickPop == null)
                        mPhotoPickPop = new CommonPopupWindow.Builder(getContext())
                                .setView(R.layout.popup_window_photo_pick)
                                .setViewOnclickListener(TripartiteFragment.this)
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
    }

    @Override
    public void getChildViewBinding(PopupWindowPhotoPickBinding view, int layoutResId) {
        RxView.clicks(view.tvTakePhoto)
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        // DESC: 拍照
                        mViewBinding.postOrderCertificate.setTag(CameraUtils.takePhoto(getActivity(), REQUEST_CAMERA));
                    }
                    mPhotoPickPop.dismiss();
                });

        RxView.clicks(view.tvFromAlbum)
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(mRxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(granted -> {
                    if (granted)
                        startActivityForResult(new Intent(getActivity(), PhotoAlbumActivity.class)
                                .putExtra(ONLY_SELECT_ONE_PHOTO, false)
                                .putExtra(CAN_SELECT_COUNT, 9 - mListDatas.size() + 1), REQUEST_LOCAL_IMAGE);
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
                    HashMap<String, Uri> result = (HashMap<String, Uri>) mViewBinding.postOrderCertificate.getTag();
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
}
