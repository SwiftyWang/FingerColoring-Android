package com.swifty.fillcolor;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.swifty.fillcolor.broadcast.LoginSuccessBroadcast;
import com.swifty.fillcolor.factory.SharedPreferencesFactory;
import com.swifty.fillcolor.listener.OnLoginSuccessListener;
import com.swifty.fillcolor.model.bean.UserBean;
import com.swifty.fillcolor.util.ImageLoaderUtil;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.UmengLoginUtil;
import com.swifty.fillcolor.util.UmengUtil;

import java.util.Locale;

/**
 * Created by Swifty.Wang on 2015/7/31.
 */
public class MyApplication extends Application {

    /*deprecated*/

    //    public static final String MAINURL = "http://www.coloring-book.info/";
//    public static final String EXTRAURL = MAINURL + "coloring/";
//    public static final String LISTURL = EXTRAURL + "coloring_page.php?id=";
    public static final String BAIDUTRANSLATEAPI = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=iD8ulydDkWhNWFmQEGEGY4m9&";
    /*deprecated*/
    public static final String SECRETGARDENLOCATION = "assets://SecretGarden/";
//    public static final String MainUrl = "http://sgdaemon.cloudapp.net/pic_dev/extAPI";
    public static final String MainUrl = "http://api.fingercoloring.com/pic/extAPI";
    public static final String ThemeListUrl = MainUrl + "/category"; // post pageid from 0
    public static final String ThemeDetailUrl = MainUrl + "/list"; // post categoryid
    public static final String ThemeThumbUrl = MainUrl + "/categorythumb?category=%d"; //get add categoryid + /category.png
    public static final String ImageThumbUrl = MainUrl + "/imageres?category=%d&image=t_%d"; //get add categoryid and imageid
    public static final String ImageLageUrl = MainUrl + "/imageres?category=%d&image=f_%d";  //get add categoryid and imageid
    public static final String UserLoginUrl = MainUrl + "/login";  //post add header token
    public static final String UserRegisterUrl = MainUrl + "/register";  //post type uid usericon gender location name
    public static final String THEMEID = "theme_id";
    public static final String BIGPIC = "bigpic";
    public static final String BIGPICFROMUSER = "bigpic_user";
    public static final String THEMENAME = "theme_name";
    public static final int PaintActivityRequest = 900;
    public static final int RepaintResult = 999;
    public static final String BIGPICFROMUSERPAINTNAME = "bigpic_user_name";


    public static int screenWidth;
    public static CharSequence SHAREWORK = "share_work";

    public static UserBean.User user;
    public static String userToken;

    @Override
    public void onCreate() {
        super.onCreate();
        initLanguage(this);
        initImageLoader();
        UmengUtil.autoUpdate(this);
        screenWidth = getScreenWidth(this);
    }

    public static void initLanguage(Context context) {
        int lancode = SharedPreferencesFactory.getInteger(context, SharedPreferencesFactory.LanguageCode, 0);
        if (lancode == 0) {
            return;
        }
        if (lancode == 1) {
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(config, dm);
        } else if (lancode == 2) {
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.locale = Locale.TRADITIONAL_CHINESE;
            resources.updateConfiguration(config, dm);
        } else if (lancode == 3) {
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.locale = Locale.ENGLISH;
            resources.updateConfiguration(config, dm);
        }
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoaderUtil.getInstance().init(config);
    }

    public static String getVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            L.e("VersionE", e.getMessage());
            e.printStackTrace();
            return "0";
        }

    }

    public static void restart(Context context) {

        if (context == null)
            return;
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(0, 0);
    }


    public static int getCurrentLanguageCode(Context context) {
        int lancode = SharedPreferencesFactory.getInteger(context, SharedPreferencesFactory.LanguageCode, 0);
        if (lancode != 0) {
            return lancode;
        } else {
            if (context.getResources().getConfiguration().locale.getCountry().toUpperCase().equals("CN"))
                return 1;
            else if (context.getResources().getConfiguration().locale.getCountry().toUpperCase().equals("TW"))
                return 2;
            else
                return 3;
        }
    }

    public static void setLanguageCode(Context context, int lancode) {
        if (lancode == 0) {
            return;
        }
        if (lancode == 1) {
            SharedPreferencesFactory.saveInteger(context, SharedPreferencesFactory.LanguageCode, 1);
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(config, dm);
            configChanged(context, config);
        } else if (lancode == 2) {
            SharedPreferencesFactory.saveInteger(context, SharedPreferencesFactory.LanguageCode, 2);
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.locale = Locale.TRADITIONAL_CHINESE;
            resources.updateConfiguration(config, dm);
            configChanged(context, config);
        } else if (lancode == 3) {
            SharedPreferencesFactory.saveInteger(context, SharedPreferencesFactory.LanguageCode, 3);
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.locale = Locale.ENGLISH;
            resources.updateConfiguration(config, dm);
            configChanged(context, config);
        }

    }

    private static void configChanged(Context context, Configuration configuration) {
        if (context instanceof Activity) {
            ((Activity) context).onConfigurationChanged(configuration);
        }
    }
}
