package com.swifty.fillcolor.model;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.swifty.fillcolor.util.ImageLoaderUtil;

public class AsynImageLoader {

    public static void setOnClearCacheFinishListener(OnClearCacheFinishListener onClearCacheFinishListener) {
        AsynImageLoader.onClearCacheFinishListener = onClearCacheFinishListener;
    }

    private static OnClearCacheFinishListener onClearCacheFinishListener;

    public static void showImageAsyn(ImageView imageView, String url) {
        if (url.contains("file://") || url.contains("drawable://")) {
            ImageLoaderUtil.getInstance().displayImage(url, imageView,
                    ImageLoaderUtil.getOptions(getBlankDrawable()),
                    new ImageLoaderUtil.AnimateFirstDisplayListener());
            return;
        }
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.getOptions(),
                new ImageLoaderUtil.AnimateFirstDisplayListener());
    }

    private static Drawable getBlankDrawable() {
        Drawable drawable;
        int color = Color.argb(255, 255, 255, 255);
        drawable = new ColorDrawable(color);
        return drawable;
    }

    public static void showImageAsynWithoutCache(ImageView imageView, String url) {
        if (url.contains("file://") || url.contains("drawable://")) {
            ImageLoaderUtil.getInstance().displayImage(url, imageView,
                    ImageLoaderUtil.getNoCacheOptions(getBlankDrawable()),
                    new ImageLoaderUtil.AnimateFirstDisplayListener());
            return;
        }
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.getNoCacheOptions(),
                new ImageLoaderUtil.AnimateFirstDisplayListener());
    }

    public static void showImageAsynWithAllCacheOpen(ImageView imageView, String url) {
        if (url.contains("file://") || url.contains("drawable://")) {
            ImageLoaderUtil.getInstance().displayImage(url, imageView,
                    ImageLoaderUtil.getOpenAllCacheOptions(getBlankDrawable()),
                    new ImageLoaderUtil.AnimateFirstDisplayListener());
            return;
        }
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.getOpenAllCacheOptions(),
                new ImageLoaderUtil.AnimateFirstDisplayListener());
    }

    public static void showImageAsynWithAllCacheOpen(ImageView imageView, String url, ImageLoadingListener imageLoadingListener) {
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.getOpenAllCacheOptions(),
                imageLoadingListener);
    }

    public static void showLagreImageAsynWithAllCacheOpen(ImageView imageView, String url, ImageLoadingListener listener) {
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.DetailImageOptions(),
                listener);
    }

    public static void showLagreImageAsynWithNoCacheOpen(ImageView imageView, String url, ImageLoadingListener listener) {
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.DetailImageOptionsNoCache(),
                listener);
    }

    public static void clearCache() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (onClearCacheFinishListener != null) {
                    onClearCacheFinishListener.clearCacheFinish();
                }
            }
        }.execute();
    }

    public interface OnClearCacheFinishListener {
        void clearCacheFinish();
    }
}
