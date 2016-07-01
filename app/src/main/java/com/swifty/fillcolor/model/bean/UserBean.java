package com.swifty.fillcolor.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Swifty.Wang on 2015/8/20.
 */
public class UserBean extends ResponseBean {
    @SerializedName("result")
    private User users;

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }


    public static class User {

        private String type;
        private String uid;
        private String usericon;
        private String gender;
        private String location;
        private String name;
        private String token;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsericon() {
            return usericon;
        }

        public void setUsericon(String usericon) {
            this.usericon = usericon;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
