package com.swifty.fillcolor.listener;

import com.swifty.fillcolor.model.bean.ThemeBean;

import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/18.
 */
public interface OnThemeListLoadListener {
    void onLoadFinish(List<ThemeBean.Theme> names);
}
