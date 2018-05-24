package com.changsukuaidi.www.widget.edittext;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.changsukuaidi.www.R;


/**
 * @Describe 可全删除的EditText
 * @Author zhouhao
 * @Date 2017/8/18
 * @Contact 605626708@qq.com
 */

public class DeleteEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {
    @DrawableRes
    public static int DEFAULT_DELETE_ICON = R.mipmap.icon_delete;
    public static int DEFAULT_PADDING_RIGHT;
    private final GestureDetector gestureDetector;

    public DeleteEditText(Context context) {
        this(context, null);
    }

    public DeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        DEFAULT_PADDING_RIGHT = (int) getResources().getDimension(R.dimen.margin_small);
        addTextChangedListener(this);
        setOnTouchListener(this);
        setOnFocusChangeListener(this);
        gestureDetector = new GestureDetector(context, listener);
        setPadding(getPaddingLeft(), getPaddingTop(), DEFAULT_PADDING_RIGHT, getPaddingBottom());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, DEFAULT_DELETE_ICON, 0);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int drawableWidth = getResources().getDrawable(DEFAULT_DELETE_ICON).getIntrinsicWidth();
        final int drawableHeight = getResources().getDrawable(DEFAULT_DELETE_ICON).getIntrinsicHeight();
        if (event.getX() >= v.getWidth() - DEFAULT_PADDING_RIGHT - drawableWidth
                && event.getY() >= v.getHeight() / 2 - drawableHeight
                && event.getY() <= v.getHeight() / 2 + drawableHeight
                && getCompoundDrawables()[2] != null) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            getText().clear();
            return true;
        }
    };

    @Override
    public void onFocusChange(View view, boolean b) {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, b && !TextUtils.isEmpty(getText()) ? DEFAULT_DELETE_ICON : 0, 0);
    }
}
