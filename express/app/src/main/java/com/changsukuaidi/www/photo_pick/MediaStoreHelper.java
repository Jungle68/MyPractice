package com.changsukuaidi.www.photo_pick;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.bean.AlbumBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by donglua on 15/5/31.
 */
class MediaStoreHelper {

    final static int INDEX_ALL_PHOTOS = 0;
    final static String SHOW_GIF = "SHOW_GIF";


    static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    private static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private PhotosResultCallback resultCallback;

        PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context.get(), args.getBoolean(SHOW_GIF, false));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;
            List<AlbumBean> albumList = new ArrayList<>();
            AlbumBean allPhoto = new AlbumBean();
            allPhoto.setName(context.get().getString(R.string.all_photo));

            while (data.moveToNext()) {
                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

                if (size < 1) continue;

                AlbumBean album = new AlbumBean();
                album.setId(bucketId);
                album.setName(name);

                if (!albumList.contains(album)) {
                    album.setCoverPath(path);
                    album.addPhoto(imageId, path);
                    album.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    albumList.add(album);
                } else {
                    albumList.get(albumList.indexOf(album)).addPhoto(imageId, path);
                }

                allPhoto.addPhoto(imageId, path);
            }
            if (allPhoto.getPhotoPaths().size() > 0) {
                allPhoto.setCoverPath(allPhoto.getPhotoPaths().get(0));
            }
            albumList.add(INDEX_ALL_PHOTOS, allPhoto);
            if (resultCallback != null) {
                resultCallback.onResultCallback(albumList);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<AlbumBean> albumDirList);
    }

}
