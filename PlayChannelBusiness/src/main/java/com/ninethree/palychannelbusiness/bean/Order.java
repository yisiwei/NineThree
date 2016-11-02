package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

/**
 * Created by user on 2016/8/2.
 */
public class Order implements Serializable {

    private String ScanHistoryId;// 7,
    private String ScanHistoryGuid;// "966c73f6-f740-472e-a0d6-3faefc0695ea",
    private String ScanHistoryText;// "使用已购产品\r\n产品扫描\r\n商家密码支付",
    private String ScanCode;// "223120124059",
    private String ScanTypeId;// "",
    private Integer ScanMethodTypeId;// 1000,
    private Integer ScanMethodId;// 23,
    private String ScanMethodGuid;// "05ad2dbf-a509-4324-8bff-f5b2265f5021",
    private String PduId;// 10,
    private String PduGuid;// "47e25458-c2c9-40c5-907c-81f60b2120ce",
    private String PduName;// "滑冰",
    private String PduText;// "",
    private String PduLogoUrl;// "http:\/\/localhost\/DUserBmFilePicWeb\/d\/214870\/20160622\/51d5cd1606e0401c83787411982764d6.jpg",
    private String Num;// 1,
    private String PduPrice;// 80.0000,
    private float PayPar;// 80.0000,
    private Integer BuyOrgId;// 214870,
    private String BuyOrgGugId;// 214870,
    private Integer SaleOrgId;// 214870,
    private String SaleOrgGuid;// "ecdb9bc0-07ce-4f3b-918a-f9357e202d7c",
    private String AddTime;// "2016/6/24 11:36:51",
    private String EditTime;// "2016/6/24 11:36:51",
    private String Reback;// "",
    private Integer StateTypeId;// 1,
    private String UseIds;// "g23;"
    private String IsSelect;// 1
    private String StoreName;// 蜂之谷游乐场

    private String NickName;
    private String UserID;
    private String Photo;
    private String Mobile;

    public String getScanHistoryId() {
        return ScanHistoryId;
    }

    public void setScanHistoryId(String scanHistoryId) {
        ScanHistoryId = scanHistoryId;
    }

    public String getScanHistoryGuid() {
        return ScanHistoryGuid;
    }

    public void setScanHistoryGuid(String scanHistoryGuid) {
        ScanHistoryGuid = scanHistoryGuid;
    }

    public String getScanHistoryText() {
        return ScanHistoryText;
    }

    public void setScanHistoryText(String scanHistoryText) {
        ScanHistoryText = scanHistoryText;
    }

    public String getScanCode() {
        return ScanCode;
    }

    public void setScanCode(String scanCode) {
        ScanCode = scanCode;
    }

    public String getScanTypeId() {
        return ScanTypeId;
    }

    public void setScanTypeId(String scanTypeId) {
        ScanTypeId = scanTypeId;
    }

    public Integer getScanMethodTypeId() {
        return ScanMethodTypeId;
    }

    public void setScanMethodTypeId(Integer scanMethodTypeId) {
        ScanMethodTypeId = scanMethodTypeId;
    }

    public Integer getScanMethodId() {
        return ScanMethodId;
    }

    public void setScanMethodId(Integer scanMethodId) {
        ScanMethodId = scanMethodId;
    }

    public String getScanMethodGuid() {
        return ScanMethodGuid;
    }

    public void setScanMethodGuid(String scanMethodGuid) {
        ScanMethodGuid = scanMethodGuid;
    }

    public String getPduId() {
        return PduId;
    }

    public void setPduId(String pduId) {
        PduId = pduId;
    }

    public String getPduGuid() {
        return PduGuid;
    }

    public void setPduGuid(String pduGuid) {
        PduGuid = pduGuid;
    }

    public String getPduName() {
        return PduName;
    }

    public void setPduName(String pduName) {
        PduName = pduName;
    }

    public String getPduText() {
        return PduText;
    }

    public void setPduText(String pduText) {
        PduText = pduText;
    }

    public String getPduLogoUrl() {
        return PduLogoUrl;
    }

    public void setPduLogoUrl(String pduLogoUrl) {
        PduLogoUrl = pduLogoUrl;
    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    public String getPduPrice() {
        return PduPrice;
    }

    public void setPduPrice(String pduPrice) {
        PduPrice = pduPrice;
    }

    public float getPayPar() {
        return PayPar;
    }

    public void setPayPar(float payPar) {
        PayPar = payPar;
    }

    public Integer getBuyOrgId() {
        return BuyOrgId;
    }

    public void setBuyOrgId(Integer buyOrgId) {
        BuyOrgId = buyOrgId;
    }

    public String getBuyOrgGugId() {
        return BuyOrgGugId;
    }

    public void setBuyOrgGugId(String buyOrgGugId) {
        BuyOrgGugId = buyOrgGugId;
    }

    public Integer getSaleOrgId() {
        return SaleOrgId;
    }

    public void setSaleOrgId(Integer saleOrgId) {
        SaleOrgId = saleOrgId;
    }

    public String getSaleOrgGuid() {
        return SaleOrgGuid;
    }

    public void setSaleOrgGuid(String saleOrgGuid) {
        SaleOrgGuid = saleOrgGuid;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getEditTime() {
        return EditTime;
    }

    public void setEditTime(String editTime) {
        EditTime = editTime;
    }

    public String getReback() {
        return Reback;
    }

    public void setReback(String reback) {
        Reback = reback;
    }

    public Integer getStateTypeId() {
        return StateTypeId;
    }

    public void setStateTypeId(Integer stateTypeId) {
        StateTypeId = stateTypeId;
    }

    public String getUseIds() {
        return UseIds;
    }

    public void setUseIds(String useIds) {
        UseIds = useIds;
    }

    public String getIsSelect() {
        return IsSelect;
    }

    public void setIsSelect(String isSelect) {
        IsSelect = isSelect;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
