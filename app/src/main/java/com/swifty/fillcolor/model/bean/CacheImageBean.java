package com.swifty.fillcolor.model.bean;

/**
 * Created by Swifty.Wang on 2015/9/9.
 */
public class CacheImageBean {
    String url;
    float wvHRadio;

    public CacheImageBean(String url, float wvHRadio) {
        this.url = url;
        this.wvHRadio = wvHRadio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getWvHRadio() {
        return wvHRadio;
    }

    public void setWvHRadio(float wvHRadio) {
        this.wvHRadio = wvHRadio;
    }
}
