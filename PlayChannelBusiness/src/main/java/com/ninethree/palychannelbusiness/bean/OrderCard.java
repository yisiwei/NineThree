package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

/**
 * Created by user on 2016/8/2.
 */
public class OrderCard implements Serializable {

    private String CardId;// 2268
    private String CardGuid;//
    private String CardNum;//
    private String CardText;//
    private float Par;//
    private float Cash;//
    private String ImgUrl;// http:\/\/img.93966.net\/O\/214700\/20160728\/24d8dc3c110a47f5b3b804cf21f14f7f.jpg
    private String TimeAdd;//

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getCardGuid() {
        return CardGuid;
    }

    public void setCardGuid(String cardGuid) {
        CardGuid = cardGuid;
    }

    public String getCardNum() {
        return CardNum;
    }

    public void setCardNum(String cardNum) {
        CardNum = cardNum;
    }

    public String getCardText() {
        return CardText;
    }

    public void setCardText(String cardText) {
        CardText = cardText;
    }

    public float getPar() {
        return Par;
    }

    public void setPar(float par) {
        Par = par;
    }

    public float getCash() {
        return Cash;
    }

    public void setCash(float cash) {
        Cash = cash;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getTimeAdd() {
        return TimeAdd;
    }

    public void setTimeAdd(String timeAdd) {
        TimeAdd = timeAdd;
    }
}
