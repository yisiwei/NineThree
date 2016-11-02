package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class SessionInfo implements Serializable {

    private static final long serialVersionUID = -2122055320227440625L;

    private User user;
    private Org org;
    private String userSecGuid;

    private boolean HasError;
    private String Message;
    private Integer ReturnCode;
    private ReturnObject ReturnObject;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public String getUserSecGuid() {
        return userSecGuid;
    }

    public void setUserSecGuid(String userSecGuid) {
        this.userSecGuid = userSecGuid;
    }

    public boolean isHasError() {
        return HasError;
    }

    public void setHasError(boolean hasError) {
        HasError = hasError;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Integer getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(Integer returnCode) {
        ReturnCode = returnCode;
    }

    public ReturnObject getReturnObject() {
        return ReturnObject;
    }

    public void setReturnObject(ReturnObject returnObject) {
        ReturnObject = returnObject;
    }
}
