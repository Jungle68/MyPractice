package com.thc.www.p2p.android.home.photo

import com.thc.www.p2p.base.BasePresenter
import javax.inject.Inject


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class PhotoPresenter @Inject constructor(repository: PhotoRepository, view: PhotoFragment)
    : BasePresenter<PhotoRepository, PhotoFragment>(repository, view) {
}