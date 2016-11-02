package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class Terminal implements Serializable {

    private static final long serialVersionUID = 1928314747496481650L;

    private String OrgName;//
    private String PduName;// "摩天轮",
    private String TerminalNum;//
    private String TerminalCode;//
    private Integer TerminalStateTypeId;
    private String UpdateTime;//
    private String EditTime;//
    private String UserName;//
    private String Photo;//

    private String DelFlag;

    public Terminal() {

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getPduName() {
        return PduName;
    }

    public void setPduName(String pduName) {
        PduName = pduName;
    }

    public String getTerminalCode() {
        return TerminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        TerminalCode = terminalCode;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getEditTime() {
        return EditTime;
    }

    public void setEditTime(String editTime) {
        EditTime = editTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Integer getTerminalStateTypeId() {
        return TerminalStateTypeId;
    }

    public void setTerminalStateTypeId(Integer terminalStateTypeId) {
        TerminalStateTypeId = terminalStateTypeId;
    }

    public String getTerminalNum() {
        return TerminalNum;
    }

    public void setTerminalNum(String terminalNum) {
        TerminalNum = terminalNum;
    }

    public String getDelFlag() {
        return DelFlag;
    }

    public void setDelFlag(String delFlag) {
        DelFlag = delFlag;
    }
}
