package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;
import java.util.List;

public class CustomerRecord implements Serializable {

    private String TypeId;// 2000,
    private String TimeAdd;// "2016/7/20 10:20:37",
    private Integer DataId;// 158,
    private float Cash;// 0.0300,

    private String PduName;// "",
    private String PduLogoUrl;// "",
    private String TradeNum;
    private String CardModelText;// "价值100元面值卡",
    private String ImgUrl;// "http:\/\/img.93966.net\/O\/216225\/20160704\/70213937d727421c9b0659d42ad026d7.png"
    private String CardTradeNum;

    private List<RecordPdu> pdus;

    public String getTypeId() {
        return TypeId;
    }

    public void setTypeId(String typeId) {
        TypeId = typeId;
    }

    public String getTimeAdd() {
        return TimeAdd;
    }

    public void setTimeAdd(String timeAdd) {
        TimeAdd = timeAdd;
    }

    public Integer getDataId() {
        return DataId;
    }

    public void setDataId(Integer dataId) {
        DataId = dataId;
    }

    public float getCash() {
        return Cash;
    }

    public void setCash(float cash) {
        Cash = cash;
    }

    public String getPduName() {
        return PduName;
    }

    public void setPduName(String pduName) {
        PduName = pduName;
    }

    public String getPduLogoUrl() {
        return PduLogoUrl;
    }

    public void setPduLogoUrl(String pduLogoUrl) {
        PduLogoUrl = pduLogoUrl;
    }

    public String getCardModelText() {
        return CardModelText;
    }

    public void setCardModelText(String cardModelText) {
        CardModelText = cardModelText;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public List<RecordPdu> getPdus() {
        return pdus;
    }

    public void setPdus(List<RecordPdu> pdus) {
        this.pdus = pdus;
    }

    public String getTradeNum() {
        return TradeNum;
    }

    public void setTradeNum(String tradeNum) {
        TradeNum = tradeNum;
    }

    public String getCardTradeNum() {
        return CardTradeNum;
    }

    public void setCardTradeNum(String cardTradeNum) {
        CardTradeNum = cardTradeNum;
    }
}

