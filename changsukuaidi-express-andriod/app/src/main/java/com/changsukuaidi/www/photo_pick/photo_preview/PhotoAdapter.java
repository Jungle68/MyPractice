package com.changsukuaidi.www.photo_pick.photo_preview;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.databinding.PhotoGalleryPageBinding;

import java.util.List;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/1/4
 * @Contact 605626708@qq.com
 */

public class PhotoAdapter extends PagerAdapter {

    private List<String> images;

    PhotoAdapter(List<String> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        PhotoGalleryPageBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                R.layout.photo_gallery_page, container, false);

        // 加载图片
        GlideApp.with(container.getContext())
                .load(images.get(position))
                .thumbnail(0.1f)
                .dontAnimate()
                .dontTransform()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mBinding.progress.setVisibility(View.GONE);
                        mBinding.photo.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(mBinding.photo);

        // 单击回执
        mBinding.photo.setOnClickListener(view -> {
            if (container.getContext() instanceof Activity) {
                if (!((Activity) container.getContext()).isFinishing()) {
                    ((Activity) container.getContext()).onBackPressed();
                }
            }
        });

        container.addView(mBinding.getRoot());

        return mBinding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
