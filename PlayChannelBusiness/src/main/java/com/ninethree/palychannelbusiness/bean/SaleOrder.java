package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2016/8/2.
 */
public class SaleOrder implements Serializable {

    private String OrderId;// 2787,
    private String OrderNumber;// "or1608225900",
    private String Create_Time;// "2016/8/22 9:25:34",
    private float TradePrice;// 0.01,
    private String Pdus;// xml

    private List<OrderGoods> orderGoods;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getCreate_Time() {
        return Create_Time;
    }

    public void setCreate_Time(String create_Time) {
        Create_Time = create_Time;
    }

    public float getTradePrice() {
        return TradePrice;
    }

    public void setTradePrice(float tradePrice) {
        TradePrice = tradePrice;
    }

    public String getPdus() {
        return Pdus;
    }

    public void setPdus(String pdus) {
        Pdus = pdus;
    }

    public List<OrderGoods> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<OrderGoods> orderGoods) {
        this.orderGoods = orderGoods;
    }
}
