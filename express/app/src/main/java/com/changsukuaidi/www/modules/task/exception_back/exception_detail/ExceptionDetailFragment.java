package com.changsukuaidi.www.modules.task.exception_back.exception_detail;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.PhotoBean;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;
import com.changsukuaidi.www.databinding.FragmentExceptionDetailBinding;
import com.changsukuaidi.www.modules.task.exception_back.SamplePhotoAdapter;
import com.changsukuaidi.www.photo_pick.photo_preview.PhotoActivity;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.recycler_itemdecoration.LinearDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static com.changsukuaidi.www.photo_pick.PhotoAlbumDetailFragment.PHOTO_PREVIEW;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/6
 * @Contact 605626708@qq.com
 */

public class ExceptionDetailFragment extends BaseFragment<ExceptionDetailPresenter, FragmentExceptionDetailBinding> implements IExceptionDetailView {
    private int mTaskType = 0; //0、配送 1、中转和物料配送
    protected SamplePhotoAdapter mSamplePhotoAdapter;
    // 图片数据源
    protected List<PhotoBean> mListDatas = new ArrayList<>();

    @Override
    protected void setPresenter() {
        DaggerExceptionDetailComp.builder()
                .appComp(BaseApplication.getAppComp())
                .exceptionDetailModule(new ExceptionDetailModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initIntent() {
        mTaskType = getActivity().getIntent().getIntExtra("task_type", 0);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_exception_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        // set title
        setToolbarTitle("异常详情");
        setToolbarLeftDisEnable();

        mListDatas.add(new PhotoBean("http://img3.imgtn.bdimg.com/it/u=2625236572,2291097099&fm=11&gp=0.jpg"));
        mListDatas.add(new PhotoBean("http://img3.imgtn.bdimg.com/it/u=2625236572,2291097099&fm=11&gp=0.jpg"));
        mListDatas.add(new PhotoBean("http://img3.imgtn.bdimg.com/it/u=2625236572,2291097099&fm=11&gp=0.jpg"));
        mListDatas.add(new PhotoBean("http://img3.imgtn.bdimg.com/it/u=2625236572,2291097099&fm=11&gp=0.jpg"));
        mListDatas.add(new PhotoBean("http://img3.imgtn.bdimg.com/it/u=2625236572,2291097099&fm=11&gp=0.jpg"));
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
                            intent.putExtra(Content.Extras.INTEGER, position);
                            startActivityForResult(intent, PHOTO_PREVIEW);
                        });
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                return false;
            }
        });

        mViewBinding.exceptionType.setText("车辆损坏");
        mViewBinding.exceptionDesc.setText("车胎突然爆裂，无法行驶，等待救援。");
        mViewBinding.tvDealTime.setText("2017-12-07 10:34:12");
        mViewBinding.tvDealWay.setText("调拨中心处理说明");
        mViewBinding.tvDealDetail.setText("POI（Point of Interest），中文可以翻译为“兴趣点”。在地理信息系统中，一个POI可以是一栋房子、一个商铺、一个邮筒、一个公交站等。\n" +
                "百度地图SDK提供三种类型的POI检索：城市内检索、周边检索和区域检索（即矩形范围检索）。下面将以POI城市内检索、周边检索 和 区域检索为例，向大家介绍如何使用检索服务");
    }
}
