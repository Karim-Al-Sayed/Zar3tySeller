package com.elasdka2.zar3tyseller.Model;

public class User {
    private String User_ID;
    private String FirstName;
    private String LastName;
    private String ImgUri;
    private String UserName;


    public User() {
    }

    public User(String firstName, String lastName, String imgUri, String userName) {
        FirstName = firstName;
        LastName = lastName;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getImgUri() {
        return ImgUri;
    }

    public void setImgUri(String imgUri) {
        ImgUri = imgUri;
    }
}
