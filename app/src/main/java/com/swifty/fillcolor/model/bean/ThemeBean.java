package com.swifty.fillcolor.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/12.
 */
public class ThemeBean extends ResponseBean {
    @SerializedName("result")
    private List<Theme> themes;

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public static class Theme {
        //category id
        private int c;
        //category name
        private String n;
        private int status;

        public Theme(int c, String n, int status) {
            this.c = c;
            this.n = n;
            this.status = status;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
