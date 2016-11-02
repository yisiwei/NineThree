package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

/**
 * Created by user on 2016/8/2.
 */
public class RecordPdu implements Serializable {

    private String PduName;// "",
    private String PduLogoUrl;// "",
    private String TradeNum;
    private String CardModelText;// "价值100元面值卡",
    private String ImgUrl;// "http:\/\/img.93966.net\/O\/216225\/20160704\/70213937d727421c9b0659d42ad026d7.png"
    private String CardTradeNum;

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
