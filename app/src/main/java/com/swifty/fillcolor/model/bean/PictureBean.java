package com.swifty.fillcolor.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/12.
 */
public class PictureBean extends ResponseBean {

    @SerializedName("result")
    private List<Picture> pictures;

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public static class Picture {

        private int id;
        private int status;
        private float wvHradio;
        private String uri; //just for sercret garden

        public Picture(int id, int status, float wvHradio) {
            this.status = status;
            this.id = id;
            this.wvHradio = wvHradio;
        }

        public Picture(String uri) {
            this.uri = uri;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public float getWvHradio() {
            return wvHradio;
        }

        public void setWvHradio(float wvHradio) {
            this.wvHradio = wvHradio;
        }
    }
}
