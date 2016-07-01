package com.swifty.fillcolor.model.bean;

/**
 * Created by Swifty.Wang on 2015/9/1.
 */
public class LocalImageBean {
    private String imageName;
    private String imageUrl;
    private String lastModDate;
    private long lastModTimeStamp;
    private float WvHRadio;

    public LocalImageBean(String imageName, String imageUrl, String lastModDate, long lastModTimeStamp, float wvHRadio) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.lastModDate = lastModDate;
        this.lastModTimeStamp = lastModTimeStamp;
        WvHRadio = wvHRadio;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(String lastModDate) {
        this.lastModDate = lastModDate;
    }

    public long getLastModTimeStamp() {
        return lastModTimeStamp;
    }

    public void setLastModTimeStamp(long lastModTimeStamp) {
        this.lastModTimeStamp = lastModTimeStamp;
    }

    public float getWvHRadio() {
        return WvHRadio;
    }

    public void setWvHRadio(long wvHRadio) {
        WvHRadio = wvHRadio;
    }
}
