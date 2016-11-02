package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class MyCustomer implements Serializable {

    private String UserID;
    private String UserGuid;
    private String UserName;
    private String NickName;
    private String Mobile;
    private String Photo;

    private float Cash;
    private String SeftOrgID;

    public MyCustomer() {

    }

    public MyCustomer(String nickName) {
        super();
        NickName = nickName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserGuid() {
        return UserGuid;
    }

    public void setUserGuid(String userGuid) {
        UserGuid = userGuid;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public float getCash() {
        return Cash;
    }

    public void setCash(float cash) {
        Cash = cash;
    }

    public String getSeftOrgID() {
        return SeftOrgID;
    }

    public void setSeftOrgID(String seftOrgID) {
        SeftOrgID = seftOrgID;
    }
}
