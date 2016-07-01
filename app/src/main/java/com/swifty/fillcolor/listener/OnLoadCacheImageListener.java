package com.swifty.fillcolor.listener;

import com.swifty.fillcolor.model.bean.CacheImageBean;

import java.util.List;

/**
 * Created by Swifty.Wang on 2015/9/9.
 */
public interface OnLoadCacheImageListener {
    void loadCacheImageSuccess(List<CacheImageBean> cacheImageBeans);
}
