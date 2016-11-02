package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

/**
 * Created by user on 2016/8/2.
 */
public class Promotion implements Serializable {

    private Integer PromotionId;
    private Integer PromotionType;

    private Integer DataId;
    private String DataTitle;
    private String DataLogo;

    private Integer OrgId;
    private String OrgGuid;

    private String Title;
    private String Note;
    private float Price;
    private String PromotionBeginDate;
    private String PromotionEndDate;
    private String UseBeginDate;
    private String UseEndDate;

    private Integer IssueNum;
    private Integer ReceiveNum;//领取数量
    private Integer UsedNum;//使用数量

    private String ADUrl;

    private String ShareTitle;
    private String ShareNote;
    private String ShareImgUrl;

    private Integer Status;

    private Integer Create_UserId;
    private String Create_Time;
    private Integer Modify_UserId;
    private String Modify_Time;

    public Integer getPromotionType() {
        return PromotionType;
    }

    public void setPromotionType(Integer promotionType) {
        PromotionType = promotionType;
    }

    public Integer getDataId() {
        return DataId;
    }

    public void setDataId(Integer dataId) {
        DataId = dataId;
    }

    public String getDataTitle() {
        return DataTitle;
    }

    public void setDataTitle(String dataTitle) {
        DataTitle = dataTitle;
    }

    public String getDataLogo() {
        return DataLogo;
    }

    public void setDataLogo(String dataLogo) {
        DataLogo = dataLogo;
    }

    public Integer getOrgId() {
        return OrgId;
    }

    public void setOrgId(Integer orgId) {
        OrgId = orgId;
    }

    public String getOrgGuid() {
        return OrgGuid;
    }

    public void setOrgGuid(String orgGuid) {
        OrgGuid = orgGuid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getPromotionBeginDate() {
        return PromotionBeginDate;
    }

    public void setPromotionBeginDate(String promotionBeginDate) {
        PromotionBeginDate = promotionBeginDate;
    }

    public String getPromotionEndDate() {
        return PromotionEndDate;
    }

    public void setPromotionEndDate(String promotionEndDate) {
        PromotionEndDate = promotionEndDate;
    }

    public String getUseBeginDate() {
        return UseBeginDate;
    }

    public void setUseBeginDate(String useBeginDate) {
        UseBeginDate = useBeginDate;
    }

    public String getUseEndDate() {
        return UseEndDate;
    }

    public void setUseEndDate(String useEndDate) {
        UseEndDate = useEndDate;
    }

    public Integer getIssueNum() {
        return IssueNum;
    }

    public void setIssueNum(Integer issueNum) {
        IssueNum = issueNum;
    }

    public String getADUrl() {
        return ADUrl;
    }

    public void setADUrl(String ADUrl) {
        this.ADUrl = ADUrl;
    }

    public String getShareTitle() {
        return ShareTitle;
    }

    public void setShareTitle(String shareTitle) {
        ShareTitle = shareTitle;
    }

    public String getShareNote() {
        return ShareNote;
    }

    public void setShareNote(String shareNote) {
        ShareNote = shareNote;
    }

    public String getShareImgUrl() {
        return ShareImgUrl;
    }

    public void setShareImgUrl(String shareImgUrl) {
        ShareImgUrl = shareImgUrl;
    }

    public Integer getCreate_UserId() {
        return Create_UserId;
    }

    public void setCreate_UserId(Integer create_UserId) {
        Create_UserId = create_UserId;
    }

    public Integer getPromotionId() {
        return PromotionId;
    }

    public void setPromotionId(Integer promotionId) {
        PromotionId = promotionId;
    }

    public Integer getReceiveNum() {
        return ReceiveNum;
    }

    public void setReceiveNum(Integer receiveNum) {
        ReceiveNum = receiveNum;
    }

    public Integer getUsedNum() {
        return UsedNum;
    }

    public void setUsedNum(Integer usedNum) {
        UsedNum = usedNum;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getCreate_Time() {
        return Create_Time;
    }

    public void setCreate_Time(String create_Time) {
        Create_Time = create_Time;
    }

    public Integer getModify_UserId() {
        return Modify_UserId;
    }

    public void setModify_UserId(Integer modify_UserId) {
        Modify_UserId = modify_UserId;
    }

    public String getModify_Time() {
        return Modify_Time;
    }

    public void setModify_Time(String modify_Time) {
        Modify_Time = modify_Time;
    }
}
