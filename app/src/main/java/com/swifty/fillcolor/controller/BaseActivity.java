package com.swifty.fillcolor.controller;

import android.content.Context;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.UmengUtil;

/**
 * Created by macpro001 on 4/8/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.initLanguage(this);
        UmengUtil.analysisOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtil.analysisOnPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // obtain current focus normally is EditText
            View v = getCurrentFocus();
            try {
                if (isShouldHideInput(v, ev)) {
                    hideSoftInput(v.getWindowToken());
                }
            } catch (Exception e) {
                L.e(e.toString());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * According compared EditText where the user clicks on the coordinates and
     * the coordinates, to determine whether to hide the keyboard, because no
     * need to hide when the user clicks the EditText
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // If the focus is not the EditText is ignored, this occurs on view just
        // been drawn, the first focus is not EditView, and the user selects
        // another focus with the trackball
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
