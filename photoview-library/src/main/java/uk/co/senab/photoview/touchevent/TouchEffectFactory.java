package uk.co.senab.photoview.touchevent;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.view.animation.Animation;

/**
 * Created by Swifty.Wang on 2015/9/8.
 */
public class TouchEffectFactory {
    TouchEffectView touchEffectView;
    private static TouchEffectFactory ourInstance = new TouchEffectFactory();

    public static TouchEffectFactory getInstance() {
        return ourInstance;
    }

    private TouchEffectFactory() {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showTouchEffect(final ViewGroup viewGroup, Context context, float x, float y) {
        touchEffectView = new TouchEffectView(context);
        touchEffectView.setTag("touchView");
        touchEffectView.setX(x - touchEffectView.maxSize / 2);
        touchEffectView.setY(y - touchEffectView.maxSize / 2);
        viewGroup.addView(touchEffectView);
        touchEffectView.post(new Runnable() {
            @Override
            public void run() {
                Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewGroup.removeView(viewGroup.findViewWithTag("touchView"));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                };
                touchEffectView.showViewWithEffect(animationListener);
            }
        });

    }
}
