package com.swifty.fillcolor.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.swifty.fillcolor.R;
import com.swifty.fillcolor.util.DensityUtil;

/**
 * Created by macpro001 on 30/5/15.
 */
public class MyDialogStyle {
    protected Dialog dialog;
    protected Context context;
    public static final int POPSTYLE = R.style.MyDialogPop;

    /**
     * randomly choose a animation
     *
     * @param context
     */
    public MyDialogStyle(Context context) {
        this.context = context;
        dialog = new Dialog(context, POPSTYLE);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MyProgressDialog.DismissDialog();
            }
        });
    }

    public void dismissDialog(Dialog.OnDismissListener onCancelListener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.setOnDismissListener(onCancelListener);
            dialog.dismiss();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void reShowDialog() {
        if (!((Activity) context).isFinishing()) {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public void showDialog() {
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    public void showOneButtonDialog(String titlestr, CharSequence Content, String btntext1, View.OnClickListener listener1, boolean cancelable) {
        dialog.setCancelable(cancelable);
        View detail_layout = View.inflate(
                context,
                R.layout.dialog_one_button, null);
        dialog.setContentView(detail_layout);
        TextView content = (TextView) detail_layout.findViewById(R.id.content);
        TextView title = (TextView) detail_layout.findViewById(R.id.title);
        if (title != null) {
            title.setText(titlestr);
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
        content.setText(Content);
        Button button1 = (Button) detail_layout.findViewById(R.id.button1);
        button1.setText(btntext1);
        button1.setOnClickListener(listener1);
        showDialog();
    }

    public void showTwoButtonDialog(CharSequence Content, String btntext1, String btntext2, View.OnClickListener listener1, View.OnClickListener listener2, boolean cancelable) {
        dialog.setCancelable(cancelable);
        View detail_layout = View.inflate(
                context,
                R.layout.dialog_two_button, null);
        dialog.setContentView(detail_layout);
        TextView content = (TextView) detail_layout.findViewById(R.id.content);
        content.setText(Content);
        Button button1 = (Button) detail_layout.findViewById(R.id.button1);
        Button button2 = (Button) detail_layout.findViewById(R.id.button2);
        button1.setText(btntext1);
        button2.setText(btntext2);
        button1.setOnClickListener(listener1);
        button2.setOnClickListener(listener2);
        showDialog();
    }

    public void showBlankDialog(String titlestr, String btntext1, View.OnClickListener listener1, boolean cancelable, View view) {
        dialog.setCancelable(cancelable);
        View detail_layout = View.inflate(
                context,
                R.layout.dialog_blank, null);
        dialog.setContentView(detail_layout);
        TextView title = (TextView) detail_layout.findViewById(R.id.title);
        FrameLayout frameLayout = (FrameLayout) detail_layout.findViewById(R.id.customcontent);
        if (title != null) {
            title.setText(titlestr);
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
        frameLayout.addView(view);
        Button button1 = (Button) detail_layout.findViewById(R.id.button1);
        button1.setText(btntext1);
        button1.setOnClickListener(listener1);
        showDialog();
    }

    public void showBlankDialog(String titlestr, View view) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        showBlankDialog(titlestr, context.getString(R.string.ok), listener, true, view);
    }

    public void showBlankDialog(String titlestr, View view, View.OnClickListener listener) {
        showBlankDialog(titlestr, context.getString(R.string.ok), listener, true, view);
    }


    protected void showTwoImageDialog(StringBuffer buffer, Drawable resId1, String s1, Drawable resId2, String s2, View.OnClickListener listener1, View.OnClickListener listener2, boolean b) {
        dialog.setCancelable(b);
        View detail_layout = View.inflate(
                context,
                R.layout.dialog_two_button, null);
        dialog.setContentView(detail_layout);
        TextView content = (TextView) detail_layout.findViewById(R.id.content);
        content.setText(buffer);
        Button button1 = (Button) detail_layout.findViewById(R.id.button1);
        Button button2 = (Button) detail_layout.findViewById(R.id.button2);
        resId1.setBounds(0, 0, DensityUtil.dip2px(context, 40), DensityUtil.dip2px(context, 40));
        resId2.setBounds(0, 0, DensityUtil.dip2px(context, 40), DensityUtil.dip2px(context, 40));
        button1.setCompoundDrawables(null, resId1, null, null);
        button2.setCompoundDrawables(null, resId2, null, null);
        button1.setText(s1);
        button1.setTextColor(context.getResources().getColor(R.color.maincolor));
        button2.setText(s2);
        button2.setTextColor(context.getResources().getColor(R.color.maincolor));
        button1.setOnClickListener(listener1);
        button2.setOnClickListener(listener2);
        showDialog();
    }
}
