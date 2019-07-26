package com.elasdka2.zar3tyseller.Model;

public class Users {
    private String User_ID;
    private String ImgUri;
    private String UserName;


    public Users() {
    }

    public Users( String imgUri, String userName) {
        ImgUri = imgUri;
        UserName = userName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getImgUri() {
        return ImgUri;
    }

    public void setImgUri(String imgUri) {
        ImgUri = imgUri;
    }
}
