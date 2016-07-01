package com.swifty.fillcolor.model;

import android.content.Context;
import android.os.AsyncTask;

import com.swifty.fillcolor.listener.OnLoadCacheImageListener;
import com.swifty.fillcolor.listener.OnLoadUserPaintListener;
import com.swifty.fillcolor.controller.main.UserFragment;
import com.swifty.fillcolor.model.bean.CacheImageBean;
import com.swifty.fillcolor.model.bean.LocalImageBean;
import com.swifty.fillcolor.util.FileUtils;
import com.swifty.fillcolor.util.L;

import java.util.List;

/**
 * Created by Swifty.Wang on 2015/9/1.
 */
public class UserFragmentModel {
    private static UserFragmentModel ourInstance;
    Context context;
    AsyncTask asyncTask;

    public static UserFragmentModel getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserFragmentModel(context);
        }
        return ourInstance;
    }

    private UserFragmentModel(Context context) {
        this.context = context;
    }

    public void obtainLocalPaintList(OnLoadUserPaintListener onLoadUserPaintListener) {
        asyncTask = new LoadLocalPaintsAsyn();
        asyncTask.execute(onLoadUserPaintListener);
    }

    public void obtainCacheImageList(Context context, OnLoadCacheImageListener onLoadCacheImageListener) {
        asyncTask = new LoadCacheImagesAsyn();
        asyncTask.execute(onLoadCacheImageListener, context);
    }

    private class LoadLocalPaintsAsyn extends AsyncTask {
        OnLoadUserPaintListener onLoadUserPaintListener;

        @Override
        protected Object doInBackground(Object[] objects) {
            L.e("load local data");
            onLoadUserPaintListener = (OnLoadUserPaintListener) objects[0];
            return FileUtils.obtainLocalImages();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            L.e(o.toString());
            if (UserFragment.getInstance().isAdded() && onLoadUserPaintListener != null) {
                onLoadUserPaintListener.loadUserPaintFinished((List<LocalImageBean>) o);
            }
        }
    }

    private class LoadCacheImagesAsyn extends AsyncTask {
        OnLoadCacheImageListener onLoadCacheImageListener;
        Context context;

        @Override
        protected Object doInBackground(Object[] params) {
            onLoadCacheImageListener = (OnLoadCacheImageListener) params[0];
            context = (Context) params[1];
            List<CacheImageBean> cacheImageBeans;
            cacheImageBeans = FCDBModel.getInstance().readHaveCacheImages(context);
            return cacheImageBeans;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (UserFragment.getInstance().isAdded() && onLoadCacheImageListener != null) {
                onLoadCacheImageListener.loadCacheImageSuccess((List<CacheImageBean>) o);
            }
        }
    }
}
