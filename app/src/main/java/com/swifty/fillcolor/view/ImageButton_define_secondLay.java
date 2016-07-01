package com.swifty.fillcolor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swifty.fillcolor.util.DensityUtil;


public class ImageButton_define_secondLay extends LinearLayout {

    private ImageView imageViewbutton;

    private TextView textView;

    public ImageButton_define_secondLay(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        imageViewbutton = new ImageView(context, attrs);
        imageViewbutton.setPadding(DensityUtil.dip2px(context,5), DensityUtil.dip2px(context,3), DensityUtil.dip2px(context,5), DensityUtil.dip2px(context,3));
        imageViewbutton.setAdjustViewBounds(true);
        imageViewbutton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        textView = new TextView(context, attrs);
        //水平居中
        textView.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(10);
        textView.setPadding(0, 0, 0, DensityUtil.dip2px(context,3));
        setClickable(true);
        setFocusable(true);
        setOrientation(LinearLayout.VERTICAL);
        addView(imageViewbutton);
        addView(textView);

    }

    public void setImageSrc(int drawableid){
        imageViewbutton.setImageResource(drawableid);
    }
}