package com.swifty.fillcolor.util;

import com.swifty.fillcolor.MyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Swifty.Wang on 2015/8/13.
 */
public class TranslateUtil {

    public static String translateByBaiduAPI(String h1) {
        MyHttpClient myHttpClient = new MyHttpClient();
        String getrest = MyApplication.BAIDUTRANSLATEAPI + "q=" + h1 + "&from=auto&to=auto";
        try {
            String ret = myHttpClient.executeGetRequest(getrest);
            if (ret != null) {
                JSONObject jsonObject = new JSONObject(ret);
                JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
                return jsonArray.getJSONObject(0).getString("dst");
            } else {
                return h1;
            }
        } catch (Exception e) {
            L.e(e.getMessage());
            return h1;
        }
    }
}
