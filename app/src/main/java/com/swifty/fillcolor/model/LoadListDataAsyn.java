package com.swifty.fillcolor.model;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.listener.OnThemeListLoadListener;
import com.swifty.fillcolor.controller.main.ThemeListFragment;
import com.swifty.fillcolor.model.bean.ThemeBean;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.MyHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/4.
 */
public class LoadListDataAsyn extends AsyncTask {

    private OnThemeListLoadListener onThemeListLoadListener;
    List<ThemeBean.Theme> themeList;
    private String PageId = "pageid";
    Context context;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            int page = (int) objects[0];
            if (objects[1] != null && objects[1] instanceof Context) {
                context = (Context) objects[1];
            }
            MyHttpClient myHttpClient = new MyHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(PageId, String.valueOf(page)));
            String ret = myHttpClient.executePostRequest(MyApplication.ThemeListUrl, params);
            Gson gson = new Gson();
            themeList = gson.fromJson(ret, ThemeBean.class).getThemes();
            //save to db
            if (themeList != null) {
                if (context != null) {
                    FCDBModel.getInstance().insertNewThemes(context, themeList);
                }
            } else {
                return "FAILED";
            }
            return "SUCCESS";
        } catch (Exception e) {
            L.e(e.toString());
            return "FAILED";
        }
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (ThemeListFragment.getInstance().isAdded() && onThemeListLoadListener != null) {
            onThemeListLoadListener.onLoadFinish(themeList);
        }
    }

    public void setOnThemeListLoadListener(OnThemeListLoadListener onThemeListLoadListener) {
        this.onThemeListLoadListener = onThemeListLoadListener;
    }
}
