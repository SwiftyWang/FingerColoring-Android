package uk.co.senab.photoview.touchevent;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

/**
 * Created by Swifty.Wang on 2015/9/8.
 */
public class TouchEffectView extends SurfaceView {
    public final int minSize = 5;
    public final int maxSize = 100;

    public TouchEffectView(Context context) {
        super(context);
        init();
    }

    public TouchEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchEffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    private void init() {
        Log.e("touch", "init");
        setLayoutParams(new FrameLayout.LayoutParams(maxSize, maxSize));
        setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_bright));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void showViewWithEffect(int duration, Animation.AnimationListener animationListener) {
        startAnimation(addAnimation(duration, animationListener));
    }

    private Animation addAnimation(int duration, Animation.AnimationListener animationListener) {
        AnimationSet animationSet = new AnimationSet(true);
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setFillAfter(true);
        animationSet.addAnimation(alphaAnimation);
        Animation largeAnimation = new ScaleAnimation(0f, 1.0f, 0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        largeAnimation.setFillAfter(true);
        animationSet.addAnimation(largeAnimation);
        animationSet.setDuration(duration);
        animationSet.setAnimationListener(animationListener);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    public void showViewWithEffect(Animation.AnimationListener animationListener) {
        showViewWithEffect(350, animationListener);
    }
}
