package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class Org implements Serializable {

    private static final long serialVersionUID = -5265570180245282106L;

    private String OrgId;
    private String OrgGuid;
    private String OrgNum;

    private String OrgName;

    private String OrgStateTypeId;
    private String OrgStytleTypeId;
    private String UserPostTypeId;

    private Integer ChannelId;

    private String BranchText;// "技术研发部",
    private String PostText;// "android工程师",
    private String UserPostTypeText;// "企业主"

    public Org() {

    }

    public String getOrgId() {
        return OrgId;
    }

    public void setOrgId(String orgId) {
        OrgId = orgId;
    }

    public String getOrgGuid() {
        return OrgGuid;
    }

    public void setOrgGuid(String orgGuid) {
        OrgGuid = orgGuid;
    }

    public String getOrgNum() {
        return OrgNum;
    }

    public void setOrgNum(String orgNum) {
        OrgNum = orgNum;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getOrgStateTypeId() {
        return OrgStateTypeId;
    }

    public void setOrgStateTypeId(String orgStateTypeId) {
        OrgStateTypeId = orgStateTypeId;
    }

    public String getOrgStytleTypeId() {
        return OrgStytleTypeId;
    }

    public void setOrgStytleTypeId(String orgStytleTypeId) {
        OrgStytleTypeId = orgStytleTypeId;
    }

    public String getUserPostTypeId() {
        return UserPostTypeId;
    }

    public void setUserPostTypeId(String userPostTypeId) {
        UserPostTypeId = userPostTypeId;
    }

    public String getBranchText() {
        return BranchText;
    }

    public void setBranchText(String branchText) {
        BranchText = branchText;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        PostText = postText;
    }

    public String getUserPostTypeText() {
        return UserPostTypeText;
    }

    public void setUserPostTypeText(String userPostTypeText) {
        UserPostTypeText = userPostTypeText;
    }

    public Integer getChannelId() {
        return ChannelId;
    }

    public void setChannelId(Integer channelId) {
        ChannelId = channelId;
    }

}
