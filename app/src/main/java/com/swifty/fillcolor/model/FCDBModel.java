package com.swifty.fillcolor.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.L;
import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.model.db.FCDBHelper;
import com.swifty.fillcolor.model.bean.CacheImageBean;
import com.swifty.fillcolor.model.bean.PictureBean;
import com.swifty.fillcolor.model.bean.ThemeBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/5.
 */
public class FCDBModel {
    private SQLiteDatabase db;
    private Cursor cursor;
    private FCDBHelper fcdbHelper;
    private static FCDBModel fcdbModel;

    private FCDBModel() {
    }

    public static FCDBModel getInstance() {
        if (fcdbModel == null) {
            fcdbModel = new FCDBModel();
        }
        return fcdbModel;
    }

    public List<ThemeBean.Theme> readThemeList(Context context) {
        fcdbHelper = new FCDBHelper(context);
        db = fcdbHelper.getReadableDatabase();
        cursor = db.query(FCDBHelper.FCTABLE, null, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            closeDBandCursor();
            return null;
        } else {
            List<ThemeBean.Theme> themeBeans = new ArrayList<>();
            while (cursor.moveToNext()) {
                themeBeans.add(new ThemeBean.Theme(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            }
            closeDBandCursor();
            return themeBeans;
        }
    }

    public void insertNewThemes(Context context, List<ThemeBean.Theme> themeBeanList) {
        fcdbHelper = new FCDBHelper(context);
        db = fcdbHelper.getWritableDatabase();
        for (ThemeBean.Theme theme : themeBeanList) {
            ContentValues value = new ContentValues();
            value.put(FCDBHelper.FCTABLE_COL_0, theme.getC());
            value.put(FCDBHelper.FCTABLE_COL_1, theme.getN());
            value.put(FCDBHelper.FCTABLE_COL_2, theme.getStatus());
            db.insert(FCDBHelper.FCTABLE, null, value);
        }
        closeDBandCursor();
    }


    public List<PictureBean.Picture> readPicList(Context context, int theme_id) {
        fcdbHelper = new FCDBHelper(context);
        db = fcdbHelper.getReadableDatabase();
        cursor = db.query(FCDBHelper.FCIMAGETABLE, null, FCDBHelper.FCIMAGETABLE_COL_0 + "=" + theme_id, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            closeDBandCursor();
            return null;
        } else {
            List<PictureBean.Picture> pictureBeans = new ArrayList<>();
            while (cursor.moveToNext()) {
                pictureBeans.add(new PictureBean.Picture(cursor.getInt(1), cursor.getInt(2), cursor.getFloat(3)));
            }
            closeDBandCursor();
            return pictureBeans;
        }
    }

    public void insertPics(Context context, int id, List<PictureBean.Picture> pictureBeans) {
        fcdbHelper = new FCDBHelper(context);
        db = fcdbHelper.getWritableDatabase();
        for (PictureBean.Picture picBean : pictureBeans) {
            ContentValues value = new ContentValues();
            value.put(FCDBHelper.FCIMAGETABLE_COL_0, id);
            value.put(FCDBHelper.FCIMAGETABLE_COL_1, picBean.getId());
            value.put(FCDBHelper.FCIMAGETABLE_COL_2, picBean.getStatus());
            value.put(FCDBHelper.FCIMAGETABLE_COL_3, picBean.getWvHradio());
            db.insert(FCDBHelper.FCIMAGETABLE, null, value);
        }
        closeDBandCursor();
    }

    private void closeDBandCursor() {
        if (cursor != null) cursor.close();
        if (db != null) db.close();
    }

    public void deleteAllRows(Context context, String table) {
        fcdbHelper = new FCDBHelper(context);
        db = fcdbHelper.getWritableDatabase();
        db.delete(table, null, null);
        closeDBandCursor();
    }

    public void deleteThisCategoryid(Context context, int categoryid, String fcimagetable) {
        fcdbHelper = new FCDBHelper(context);
        db = fcdbHelper.getWritableDatabase();
        db.delete(fcimagetable, FCDBHelper.FCIMAGETABLE_COL_0 + " = " + categoryid, null);
        closeDBandCursor();
    }

//    public void addMD5andHashCode(SQLiteDatabase sqLiteDatabase) {
//        db = sqLiteDatabase;
//        cursor = sqLiteDatabase.query(FCDBHelper.FCIMAGETABLE, null, null, null, null, null, null);
//        if (cursor.                                                                                             getCount() == 0) {
//            return;
//        } else {
//            Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
//            while (cursor.moveToNext()) {
//                String url = String.format(MyApplication.ImageLageUrl, cursor.getInt(0), cursor.getInt(1));
//                ContentValues values = new ContentValues();
//                values.put(FCDBHelper.FCIMAGETABLE_COL_4, md5FileNameGenerator.generate(url));
//                values.put(FCDBHelper.FCIMAGETABLE_COL_5, String.valueOf(url.hashCode()));
//                db.update(FCDBHelper.FCIMAGETABLE, values, FCDBHelper.FCIMAGETABLE_COL_0 + "=" + cursor.getInt(0) + " and " + FCDBHelper.FCIMAGETABLE_COL_1 + "=" + cursor.getInt(1), null);
//            }
//        }
//    }

    public List<CacheImageBean> readHaveCacheImages(Context context) {
        fcdbHelper = new FCDBHelper(context);
        db = fcdbHelper.getWritableDatabase();
        cursor = db.query(FCDBHelper.FCIMAGETABLE, null, null, null, null, null, null);
        List<CacheImageBean> cacheImageBeans = new ArrayList<>();
        if (cursor.getCount() == 0) {
            closeDBandCursor();
            return cacheImageBeans;
        } else {
            while (cursor.moveToNext()) {
                String url = String.format(MyApplication.ImageLageUrl, cursor.getInt(0), cursor.getInt(1));
                File file = DiskCacheUtils.findInCache(url, ImageLoader.getInstance().getDiskCache());
                if (file != null && file.exists()) {
                    Float aFloat = 0f;
                    try{
                        aFloat = cursor.getFloat(3);
                    }catch (Exception e){
                        L.e(e.toString());
                    }
                    cacheImageBeans.add(new CacheImageBean(url, aFloat));
                }
            }
            return cacheImageBeans;
        }
    }
}
