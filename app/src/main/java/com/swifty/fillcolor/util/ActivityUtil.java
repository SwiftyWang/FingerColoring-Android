package com.swifty.fillcolor.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Swifty.Wang on 2015/8/12.
 */
public class ActivityUtil {

    public static void hideStatusBar(final Activity context) {
        hideBar(context);
        if (Build.VERSION.SDK_INT >= 11) {
            View decorView = context.getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            // Note that system bars will only be "visible" if none of the
                            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                // TODO: The system bars are visible. Make any desired
                                // adjustments to your UI, such as showing the action bar or
                                // other navigational controls.
                                showBar(context);
                                L.e("show");
                            } else {
                                // TODO: The system bars are NOT visible. Make any desired
                                // adjustments to your UI, such as hiding the action bar or
                                // other navigational controls.
                                L.e("hide");
                                hideBar(context);
                            }
                        }
                    });
        }
    }

    private static void showBar(Activity context) {
        if (Build.VERSION.SDK_INT < 16) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private static void hideBar(Activity context) {
        if (Build.VERSION.SDK_INT < 16) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
