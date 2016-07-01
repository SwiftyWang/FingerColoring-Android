package com.swifty.fillcolor.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.swifty.fillcolor.listener.OnUnLockImageSuccessListener;
import com.swifty.fillcolor.R;


/**
 * Created by Swifty.Wang on 2015/7/3.
 */
public class SNSUtil {

    public static void shareApp(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getString(R.string.sharecontent);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.pleaseselect)));

    }

    public static void shareApp(Context context, OnUnLockImageSuccessListener onUnLockImageSuccessListener) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getString(R.string.sharecontent);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.pleaseselect)));
        onUnLockImageSuccessListener.UnlockImageSuccess();
    }

    /**
     * *************
     * <p/>
     * 发起添加群流程。群号：手指填图 用户交流群(104368068) 的 key 为： 7yxN_oUZcVfWCDOZqS8qvJkl0tgOKj3Q
     * 调用 joinQQGroup(7yxN_oUZcVfWCDOZqS8qvJkl0tgOKj3Q) 即可发起手Q客户端申请加群 手指填图 用户交流群(104368068)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     * ****************
     */
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static void joinQQGroup(Context context) {

        if (!joinQQGroup(context, "7yxN_oUZcVfWCDOZqS8qvJkl0tgOKj3Q")) {
            Toast.makeText(context, context.getString(R.string.joinGroupFailed), Toast.LENGTH_SHORT).show();
        }
    }

}
