package com.swifty.fillcolor.factory;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Swifty.Wang on 2015/6/26.
 */
public class SharedPreferencesFactory {
    private static final String Name = "Cache";
    public static final String StackSize = "stack_max_size";
    public static final String CommentEnableKey = "comment_enable_key";
    public static final String AddQQGroupEnable = "add_QQGroup_enable";
    public static final String PaintHint = "paint_hint1";
    public static final String PaintHint2 = "paint_hint2";
    public static final String SavedColor1 = "saved_color1";
    public static final String SavedColor2 = "saved_color2";
    public static final String SavedColor3 = "saved_color3";
    public static final String SavedColor4 = "saved_color4";
    public static final String ImageStatus = "image_status";
    public static final String BuXianButtonClickDialogEnable = "buxian_button_click_dialog_enable";
    public static final String BuXianFirstPointDialogEnable = "buxian_firstpoint_dialog_enable";
    public static final String BuXianNextPointDialogEnable = "buxian_nextpoint_dialog_enable";
    public static final String PickColorDialogEnable = "pick_color_dialog_enable";
    /**
     * 0:system default
     * 1:CN
     * 2：TW
     * 3: EN
     */
    public static final String LanguageCode = "language_code";
    public static final String USERSESSION = "user_session";
    public static final java.lang.String IsFirstTimeShowLoginDialog = "first_time_show_login";
    public static final String GradualModel ="gradual_model";


    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String grabString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, true);
        } else {
            saveBoolean(context, key, true);
            return false;
        }
    }

    public static boolean getBoolean(Context context, String key, boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, b);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static int getInteger(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 10);
    }

    public static int getInteger(Context context, String key, Integer def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, def);
    }

    public static void saveInteger(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
