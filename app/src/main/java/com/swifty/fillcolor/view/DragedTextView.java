package com.swifty.fillcolor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class DragedTextView extends TextView {
    private static final String TAG = "qt";
    private int mPreviousx = 0;
    private int mPreviousy = 0;
    // a array for save the drag position
    private int[] mCurrentLayout = new int[4];

    public int[] getCurrentLayout() {
        return mCurrentLayout;
    }

    public DragedTextView(Context context) {
        super(context);
    }

    public DragedTextView(Context context, AttributeSet attribute) {
        super(context, attribute, 0);
    }

    public DragedTextView(Context context, AttributeSet attribute, int style) {
        super(context, attribute, style);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentLayout[0] != 0 || mCurrentLayout[1] != 0 || mCurrentLayout[2] != 0 || mCurrentLayout[3] != 0)
            layout(mCurrentLayout[0], mCurrentLayout[1], mCurrentLayout[2], mCurrentLayout[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int iAction = event.getAction();
        final int iCurrentx = (int) event.getX();
        final int iCurrenty = (int) event.getY();
        switch (iAction) {
            case MotionEvent.ACTION_DOWN:
                mPreviousx = iCurrentx;
                mPreviousy = iCurrenty;
                break;
            case MotionEvent.ACTION_MOVE:
                int iDeltx = iCurrentx - mPreviousx;
                int iDelty = iCurrenty - mPreviousy;
                final int iLeft = getLeft();
                final int iTop = getTop();
                if (iDeltx != 0 || iDelty != 0) {
                    mCurrentLayout[0] = iLeft + iDeltx;
                    mCurrentLayout[1] = iTop + iDelty;
                    mCurrentLayout[2] = iLeft + iDeltx + getWidth();
                    mCurrentLayout[3] = iTop + iDelty + getHeight();
                    postInvalidate();
                }
                mPreviousx = iCurrentx - iDeltx;
                mPreviousy = iCurrenty - iDelty;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
}
