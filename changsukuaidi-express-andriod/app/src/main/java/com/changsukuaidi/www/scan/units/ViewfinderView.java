/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.changsukuaidi.www.scan.units;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.google.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 100L;
    private static final int OPAQUE = 0xFF;

    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int resultPointColor;
    private final int themeColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private Bitmap scanLine, drawedScanLine, ltCorner, rtCorner, lbCorner, rbCorner;
    private Paint textPaint;
    private int radix = 0;
    private int textWidth, textHeight;
    private String hint;


    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every
        // time in onDraw().
        paint = new Paint();
        textPaint = new Paint();
        Rect rect = new Rect();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        themeColor = resources.getColor(R.color.themeColor);
        possibleResultPoints = new HashSet<>(5);
        scanLine = BitmapFactory.decodeResource(getResources(), R.mipmap.saoma_1);
        ltCorner = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_frame_center_1);
        rtCorner = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_frame_center_2);
        lbCorner = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_frame_center_3);
        rbCorner = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_frame_center_4);

        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DisplayUtils.sp2px(getContext(), 12));
        hint = getContext().getResources().getString(R.string.scan_hint);
        textPaint.getTextBounds(hint, 0, hint.length(), rect);
        textWidth = rect.width();
        textHeight = rect.height();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int w = DisplayUtils.dp2px(getContext(), 4);
        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top + w, paint);//上方矩形
        canvas.drawRect(0, frame.top + w, frame.left + w, frame.bottom - w, paint);//左边
        canvas.drawRect(frame.right - w, frame.top + w, width, frame.bottom - w, paint);//右边
        canvas.drawRect(0, frame.bottom - w, width, height, paint);//下方
        paint.setColor(themeColor);
        paint.setStrokeWidth(DisplayUtils.dp2px(getContext(), 1));
        canvas.drawLine(frame.left, frame.top + w, frame.right, frame.top + w, paint);
        canvas.drawLine(frame.left + w, frame.top, frame.left + w, frame.bottom, paint);
        canvas.drawLine(frame.right - w, frame.top, frame.right - w, frame.bottom, paint);
        canvas.drawLine(frame.left, frame.bottom - w, frame.right, frame.bottom - w, paint);

        paint.setAlpha(OPAQUE);
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {
            canvas.drawBitmap(ltCorner, frame.left, frame.top, paint);
            canvas.drawBitmap(rtCorner, frame.right - rtCorner.getWidth(), frame.top, paint);
            canvas.drawBitmap(lbCorner, frame.left, frame.bottom - lbCorner.getHeight(), paint);
            canvas.drawBitmap(rbCorner, frame.right - rbCorner.getWidth(), frame.bottom - rbCorner.getHeight(), paint);

            // Draw scan line
            if (drawedScanLine == null) {
                drawedScanLine = Bitmap.createScaledBitmap(scanLine, frame.width(), scanLine.getHeight(), false);
                scanLine.recycle();
            }
            int top = frame.top + 20 * radix;
            if (top >= frame.bottom - 40) {
                radix = 0;
                top = frame.top;
            } else {
                radix++;
            }
            canvas.drawBitmap(drawedScanLine, frame.left, top, paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }

            // Request another update at the animation interval, but only
            // repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
            canvas.drawText(hint, frame.left - (textWidth - frame.width()) / 2, frame.bottom + textHeight + DisplayUtils.dp2px(getContext(), 20), textPaint);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
