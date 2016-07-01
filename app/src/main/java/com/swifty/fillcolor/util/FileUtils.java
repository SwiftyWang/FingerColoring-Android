package com.swifty.fillcolor.util;

import android.graphics.BitmapFactory;
import android.os.Environment;

import com.swifty.fillcolor.controller.main.TimestampComparator;
import com.swifty.fillcolor.model.bean.LocalImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Swifty.Wang on 2015/9/1.
 */
public class FileUtils {

    public static List<LocalImageBean> obtainLocalImages() {
        List<LocalImageBean> localImageBeans = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getPath() + "/MyFCWorks";
        File f = new File(path);
        if (f != null && f.listFiles() != null) {
            File file[] = f.listFiles();
            for (int i = 0; i < file.length; i++) {
                if (numberFileName(file[i].getName())) {
                    localImageBeans.add(new LocalImageBean(file[i].getName(), path + "/" + file[i].getName(), DateTimeUtil.formatTimeStamp(file[i].lastModified()), file[i].lastModified(), getDropboxIMGSize(file[i])));
                    L.d("Files", path + "/" + file[i].getName() + ", " + file[i].lastModified());
                }
            }
        }
        TimestampComparator timeComparator = new TimestampComparator();
        Collections.sort(localImageBeans, timeComparator);
        return localImageBeans;
    }

    private static boolean numberFileName(String name) {
        String formatName = name.replace(".png", "").replace(".jpg", "");
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(formatName).matches();
    }

    public static boolean deleteFile(String url) {
        File file = new File(url.replace("file://", ""));
        if (file != null) {
            return file.delete();
        } else {
            return false;
        }
    }

    public static void deleteAllPaints() {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/MyFCWorks");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
            dir.delete();
        }
    }

    private static float getDropboxIMGSize(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        float imageHeight = options.outHeight;
        float imageWidth = options.outWidth;
        return imageWidth / imageHeight;
    }
}
