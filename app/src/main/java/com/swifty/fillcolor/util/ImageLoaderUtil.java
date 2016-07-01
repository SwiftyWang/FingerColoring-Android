package com.swifty.fillcolor.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.swifty.fillcolor.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ImageLoaderUtil {
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    private ImageLoaderUtil() {
    }

    public static ImageLoader getInstance() {
        return imageLoader;
    }

    public static DisplayImageOptions DetailImageOptionsNoCache() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.blank)
                .showImageForEmptyUri(R.mipmap.blank)
                .showImageOnFail(R.mipmap.blank).cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.NONE).build();
        return options;
    }

    public static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (imageUri.equals(view.getTag())) {
                L.e(String.valueOf(view.getTag()));
                ((ImageView)view).setImageBitmap(loadedImage);
                return;
            }
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(getRandomColorDrawable())
                .showImageForEmptyUri(R.mipmap.loading14)
                .showImageOnFail(R.mipmap.loading14).cacheInMemory(false)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }

    public static DisplayImageOptions getNoCacheOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(getRandomColorDrawable())
                .showImageForEmptyUri(R.mipmap.loading14)
                .showImageOnFail(R.mipmap.loading14).cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }

    public static DisplayImageOptions getOpenAllCacheOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(getRandomColorDrawable())
                .showImageForEmptyUri(R.mipmap.loading14)
                .showImageOnFail(R.mipmap.loading14).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }

    public static DisplayImageOptions getOptions(Drawable loadingdrawable) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingdrawable)
                .showImageForEmptyUri(R.mipmap.loading14)
                .showImageOnFail(R.mipmap.loading14).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }

    public static DisplayImageOptions getNoCacheOptions(Drawable loadingdrawable) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingdrawable)
                .showImageForEmptyUri(R.mipmap.loading14)
                .showImageOnFail(R.mipmap.loading14).cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }

    public static DisplayImageOptions getOpenAllCacheOptions(Drawable loadingdrawable) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingdrawable)
                .showImageForEmptyUri(R.mipmap.loading14)
                .showImageOnFail(R.mipmap.loading14).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }

    public static DisplayImageOptions DetailImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.blank)
                .showImageForEmptyUri(R.mipmap.blank)
                .showImageOnFail(R.mipmap.blank).cacheInMemory(false)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.NONE).build();
        return options;
    }

    private static Drawable getRandomColorDrawable() {
        Drawable drawable;
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        drawable = new ColorDrawable(color);
        return drawable;
    }

}
