package com.changsukuaidi.www.scan;

import android.content.res.AssetFileDescriptor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.WindowManager;
import android.widget.Toast;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.databinding.ActivityScanBinding;
import com.changsukuaidi.www.databinding.PopupScanResultBinding;
import com.changsukuaidi.www.scan.units.CameraManager;
import com.changsukuaidi.www.scan.units.CaptureActivityHandler;
import com.changsukuaidi.www.scan.units.InactivityTimer;
import com.changsukuaidi.www.scan.units.ViewfinderView;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Initial the camera 扫描二维码
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends AppCompatActivity implements Callback, CommonPopupWindow.ViewInterface<PopupScanResultBinding> {
    private static final float BEEP_VOLUME = 0.10f;
    private CommonPopupWindow mResultPopWindow;

    private ActivityScanBinding mScanBinding;

    private CaptureActivityHandler handler;
    private Vector<BarcodeFormat> decodeFormats;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;

    private String characterSet;
    private boolean playBeep;
    private boolean hasSurface;
    private boolean vibrate;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanBinding = DataBindingUtil.setContentView(this, R.layout.activity_scan);
        CameraManager.init(getApplication());
        // 有标题的时候都给statusBar的背景设置为themeColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.themeColor));
        }

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        RxView.clicks(mScanBinding.toolbarLeft)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = mScanBinding.previewView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        // FIXME
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            showResultPop();
        }
    }

    private void showResultPop() {
        if (mResultPopWindow == null) {
            mResultPopWindow = new CommonPopupWindow.Builder(this)
                    .setView(R.layout.popup_scan_result)
                    .setWidthAndHeight(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setAnimationStyle(0)
                    .setViewOnclickListener(this)
                    .create();
        }
        mResultPopWindow.setBackGroundLevel(0.75F);
        mResultPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 使Zxing能够继续扫描
     */
    public void continuePreview() {
        if (handler != null) {
            Message message = new Message();
            message.what = R.id.restart_preview;
            handler.handleMessage(message);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return mScanBinding.viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        mScanBinding.viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = mediaPlayer -> mediaPlayer.seekTo(0);

    @Override
    public void getChildViewBinding(PopupScanResultBinding view, int layoutResId) {
        RxView.clicks(view.tvScanResultOk)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    continuePreview();
                    mResultPopWindow.dismiss();
                });
    }
}