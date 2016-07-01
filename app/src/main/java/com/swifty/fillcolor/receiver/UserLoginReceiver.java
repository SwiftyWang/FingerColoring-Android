package com.swifty.fillcolor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.controller.main.MainActivity;
import com.swifty.fillcolor.model.AsynImageLoader;
import com.swifty.fillcolor.util.DensityUtil;
import com.swifty.fillcolor.util.ImageLoaderUtil;

/**
 * Created by Swifty on 2015/10/3.
 */
public class UserLoginReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = null;
        if (intent.hasExtra("msg")) {
            action = intent.getStringExtra("msg");
        }
        if (context instanceof MainActivity) {
            if ("loginsuccess".equals(action)) {
                if (MyApplication.user != null) {

                    ImageLoader.getInstance().loadImage(MyApplication.user.getUsericon(), new ImageSize(DensityUtil.dip2px(context, 32),DensityUtil.dip2px(context,32)),ImageLoaderUtil.getOpenAllCacheOptions(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            ((MainActivity) context).getSupportActionBar().setIcon(new BitmapDrawable(context.getResources(), bitmap));
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                    ((MainActivity) context).setTitle(MyApplication.user.getName());
                    if (MainActivity.logout != null) {
                        MainActivity.logout.setVisible(true);
                    }
                }
            } else if ("logoutsuccess".equals(action)) {
                ((MainActivity) context).getSupportActionBar().setIcon(0);
                ((MainActivity) context).setTitle(context.getString(R.string.app_name));
                if (MainActivity.logout != null) {
                    MainActivity.logout.setVisible(false);
                }
            }
        }
    }

}

