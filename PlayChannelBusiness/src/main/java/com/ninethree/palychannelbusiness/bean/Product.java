package com.ninethree.palychannelbusiness.bean;

import android.content.Intent;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = 1928314747496481650L;

    private Integer PduId;// 1,
    private String PduGuid;// "f99a6a02-e267-40d0-b2b2-61e73afa4dce",
    private String LogoUrl;// "",
    private String PduName;// "摩天轮游乐卷",
    private String KeyWordName;// "摩天轮",
    private Integer StateTypeId;// 0,
    private Integer NumReptory;// 10,
    private Integer NumSaled;// 0,
    private Integer NumBooking;// 0,
    private float Price;// 1.00,
    private float PriceFinal;// 2.00,
    private String Unit;// "张",
    private Integer OrgId;// 214870,
    private String OrgGuid;// "ecdb9bc0-07ce-4f3b-918a-f9357e202d7c",
    private Integer DelFlag;// 0,
    private String ClassId;// 1,
    private Integer OrgPduClassId;// 0,
    private String OrgPduClassGuid;// "",
    private String Modify_Time;// "2016/5/9 14:00:23",
    private String DFormatParas;// "",
    private String DIntroduces;// "",
    private String DPacks;// "",
    private String DSaleServices;// ""

    private String TerminalIds;

    public Product() {

    }
    public Product(Integer pduId, String pduName) {
        PduId = pduId;
        PduName = pduName;
    }

    public Integer getPduId() {
        return PduId;
    }

    public void setPduId(Integer pduId) {
        PduId = pduId;
    }

    public String getPduGuid() {
        return PduGuid;
    }

    public void setPduGuid(String pduGuid) {
        PduGuid = pduGuid;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public String getPduName() {
        return PduName;
    }

    public void setPduName(String pduName) {
        PduName = pduName;
    }

    public String getKeyWordName() {
        return KeyWordName;
    }

    public void setKeyWordName(String keyWordName) {
        KeyWordName = keyWordName;
    }

    public Integer getStateTypeId() {
        return StateTypeId;
    }

    public void setStateTypeId(Integer stateTypeId) {
        StateTypeId = stateTypeId;
    }

    public Integer getNumReptory() {
        return NumReptory;
    }

    public void setNumReptory(Integer numReptory) {
        NumReptory = numReptory;
    }

    public Integer getNumSaled() {
        return NumSaled;
    }

    public void setNumSaled(Integer numSaled) {
        NumSaled = numSaled;
    }

    public Integer getNumBooking() {
        return NumBooking;
    }

    public void setNumBooking(Integer numBooking) {
        NumBooking = numBooking;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public float getPriceFinal() {
        return PriceFinal;
    }

    public void setPriceFinal(float priceFinal) {
        PriceFinal = priceFinal;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
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

    public Integer getDelFlag() {
        return DelFlag;
    }

    public void setDelFlag(Integer delFlag) {
        DelFlag = delFlag;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public Integer getOrgPduClassId() {
        return OrgPduClassId;
    }

    public void setOrgPduClassId(Integer orgPduClassId) {
        OrgPduClassId = orgPduClassId;
    }

    public String getOrgPduClassGuid() {
        return OrgPduClassGuid;
    }

    public void setOrgPduClassGuid(String orgPduClassGuid) {
        OrgPduClassGuid = orgPduClassGuid;
    }

    public String getModify_Time() {
        return Modify_Time;
    }

    public void setModify_Time(String modify_Time) {
        Modify_Time = modify_Time;
    }

    public String getDFormatParas() {
        return DFormatParas;
    }

    public void setDFormatParas(String dFormatParas) {
        DFormatParas = dFormatParas;
    }

    public String getDIntroduces() {
        return DIntroduces;
    }

    public void setDIntroduces(String dIntroduces) {
        DIntroduces = dIntroduces;
    }

    public String getDPacks() {
        return DPacks;
    }

    public void setDPacks(String dPacks) {
        DPacks = dPacks;
    }

    public String getDSaleServices() {
        return DSaleServices;
    }

    public void setDSaleServices(String dSaleServices) {
        DSaleServices = dSaleServices;
    }

    public String getTerminalIds() {
        return TerminalIds;
    }

    public void setTerminalIds(String terminalIds) {
        TerminalIds = terminalIds;
    }
}
