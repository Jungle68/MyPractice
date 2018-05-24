package com.thc.www.p2p.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import java.io.ByteArrayOutputStream
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth




/**
 * @Describe
 * @Author thc
 * @Date  2018/5/22
 */
object FileUtils {
    fun bitmap2StrByByte(bit: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.JPEG, 100, baos)//参数100表示不压缩
        return baos.toByteArray()
    }

    fun byte2Bitmap(bytes: ByteArray): Bitmap =
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)


    /**
     * 通过Base64将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    fun bitmap2StrByBase64WithColor(bit: Bitmap, bgColor: Int): String {
        val width = bit.width
        val height = bit.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        canvas.drawColor(bgColor)
        val baseRect = Rect(0, 0, width, height)
        val frontRect = Rect(0, 0, width, height)
        canvas.drawBitmap(bit, frontRect, baseRect, null)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos) //参数100表示不压缩
        val bytes = bos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    // 耗时较长
    fun getApkIcon(context: Context, apkPath: String): Drawable? {
        Log.e("Test", System.currentTimeMillis().toString())
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES)
        if (info != null) {
            val appInfo = info.applicationInfo
            appInfo.sourceDir = apkPath
            appInfo.publicSourceDir = apkPath
            try {
                Log.e("Test", System.currentTimeMillis().toString())
                return appInfo.loadIcon(pm)
            } catch (e: OutOfMemoryError) {
                Log.e("ApkIconLoader", e.toString())
            }

        }
        return null
    }
}