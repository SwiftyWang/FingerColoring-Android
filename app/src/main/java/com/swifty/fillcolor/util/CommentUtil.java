package com.swifty.fillcolor.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.swifty.fillcolor.listener.OnUnLockImageSuccessListener;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.factory.SharedPreferencesFactory;


/**
 * Created by Swifty.Wang on 2015/7/3.
 */
public class CommentUtil {
    public static void commentApp(Context context) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
            SharedPreferencesFactory.saveBoolean(context, SharedPreferencesFactory.CommentEnableKey, false);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.commentFailed), Toast.LENGTH_SHORT).show();
        }
    }

    public static void commentApp(Context context, OnUnLockImageSuccessListener onUnLockImageSuccessListener) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
            SharedPreferencesFactory.saveBoolean(context, SharedPreferencesFactory.CommentEnableKey, false);
            onUnLockImageSuccessListener.UnlockImageSuccess();
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.commentFailed), Toast.LENGTH_SHORT).show();
        }
    }
}
