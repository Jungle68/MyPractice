package com.changsukuaidi.www.photo_pick;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.GlideApp;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/1/11
 * @Contact 605626708@qq.com
 */

public class CameraUtils {

    /**
     * 改动这个路径时需要同步改动res/file_paths.xml下路径
     */
    private static final String cache = "cskd/image_cache";

    public static final int UPDATE_HEAD = 100;

    @IntDef({UPDATE_HEAD})
    @interface UPDATE {

    }

    /**
     * 检查外置存储是否存在
     */
    private static boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取外置存储路径
     */
    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString() + "/";
    }

    /**
     * 拍照
     *
     * @param activity    activity
     * @param requestCode requestCode
     * @return
     */
    @Nullable
    public static HashMap<String, Uri> takePhoto(Activity activity, int requestCode) {
        HashMap<String, Uri> result = new HashMap<>();
        if (!isHasSdcard()) return null;

        String path = getSDPath() + cache + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        //第二参数是在manifest.xml定义 provider的authorities属性
        Uri photoUri = FileProvider.getUriForFile(activity, "com.cskd.express.fileprovider", file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(activity.getContentResolver(), "A photo", photoUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    activity.getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        activity.startActivityForResult(intent, requestCode);
        result.put(path, photoUri);
        return result;
    }

    /**
     * 裁剪图片
     *
     * @param sourceUri 原始uri
     * @param type      裁剪类型
     */
    public static void cropPhotoByPath(Activity activity, Uri sourceUri, @UPDATE int type) {
        String path = getSDPath() + "/" + cache + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(file));
        UCrop.Options options = new UCrop.Options();
        switch (type) {
            case UPDATE_HEAD:
                uCrop.withAspectRatio(1, 1); // 1:1
                options.setToolbarTitle(activity.getString(R.string.crop_photo));
                break;
            default:
                break;
        }
        options.setStatusBarColor(activity.getResources().getColor(R.color.themeColor));
        options.setToolbarColor(activity.getResources().getColor(R.color.themeColor));
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);    // 图片质量压缩
        options.setCircleDimmedLayer(false); // 是否裁剪圆形
        options.setHideBottomControls(true);// 是否隐藏底部的控制面板
        options.setCropFrameColor(Color.TRANSPARENT);// 设置内矩形边框线条颜色
        options.setShowCropGrid(false);// 是否展示内矩形的分割线
        uCrop.withOptions(options);

        uCrop.start(activity);

    }

    /**
     * 设置imageView 图片 uri
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        ContentResolver contentProvider = context.getContentResolver();
        ParcelFileDescriptor mInputPFD;
        try {
            //获取contentProvider图片
            mInputPFD = contentProvider.openFileDescriptor(uri, "r");
            if (mInputPFD != null) {
                FileDescriptor fileDescriptor = mInputPFD.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
