package com.swifty.fillcolor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swifty.fillcolor.util.DensityUtil;


public class ImageCheckBox_define extends LinearLayout implements Checkable, View.OnClickListener {
    private static final int[] STATE_CHECKABLE = {android.R.attr.state_pressed};
    private boolean mChecked;
    private CheckedImageView imageViewbutton;
    private TextView textView;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public ImageCheckBox_define(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        imageViewbutton = new CheckedImageView(context, attrs);
        imageViewbutton.setPadding(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 3), DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 3));
        imageViewbutton.setAdjustViewBounds(true);
        imageViewbutton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        textView = new TextView(context, attrs);
        //水平居中
        textView.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(10);
        textView.setPadding(0, 0, 0, DensityUtil.dip2px(context, 3));
        setClickable(true);
        setFocusable(true);
        setOrientation(LinearLayout.VERTICAL);
        addView(imageViewbutton);
        addView(textView);
        setOnClickListener(this);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }
        mChecked = checked;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
        }
        imageViewbutton.refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
        }
    }

    @Override
    public void onClick(View v) {
        mChecked = !mChecked;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
        }
        imageViewbutton.refreshDrawableState();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mChecked) {
            mergeDrawableStates(drawableState, STATE_CHECKABLE);
        }
        return drawableState;
    }

    public class CheckedImageView extends ImageView{


        public CheckedImageView(Context context) {
            super(context);
        }

        public CheckedImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CheckedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        @Override
        public int[] onCreateDrawableState(int extraSpace) {
            int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            if (mChecked) {
                mergeDrawableStates(drawableState, STATE_CHECKABLE);
            }
            return drawableState;
        }

    }

    public void setText(int rid){
        textView.setText(rid);
    }
    public void setText(String str){
        textView.setText(str);
    }
}