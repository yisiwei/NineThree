package com.ninethree.palychannelbusiness.util;

import android.util.Xml;

import com.ninethree.palychannelbusiness.bean.OrderGoods;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class PullParseXmlUtil {

    /**
     * <Pdus>
     * <EPdu>
     * <OrderGoodsId>2268</OrderGoodsId>
     * <OrderGoodsGuid>C269AA2D-0ACF-4308-85AF-855D636D2749</OrderGoodsGuid>
     * <PduId>717</PduId>
     * <PduGuid>32013824-6387-43BE-8193-E6E8B27DD5BA</PduGuid>
     * <PduName>摩天轮</PduName>
     * <PduLogoUrl>http:\/\/img.93966.net\/O\/214700\/20160728\/24d8dc3c110a47f5b3b804cf21f14f7f.jpg</PduLogoUrl>
     * <TradePrice>0.01</TradePrice>
     * <TradeNum>1</TradeNum>
     * <Unit>次</Unit>
     * <Price>9.00</Price>
     * <OrderId>2787</OrderId>
     * </EPdu>
     * </Pdus>
     */
    public static List<OrderGoods> getOrderGoods(String xml)
            throws XmlPullParserException, IOException {
        List<OrderGoods> orderGoods = null;
        OrderGoods orderGood = null;
        XmlPullParser pullParser = Xml.newPullParser();
        // 为Pull解析器设置要解析的XML数据
        //pullParser.setInput(xml, "UTF-8");
        pullParser.setInput(new StringReader(xml));
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    orderGoods = new ArrayList<OrderGoods>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("EPdu".equals(pullParser.getName())) {
                        orderGood = new OrderGoods();
                    }
                    if ("OrderGoodsId".equals(pullParser.getName())) {
                        orderGood.setOrderGoodsId(pullParser.nextText());
                    }
                    if ("OrderGoodsGuid".equals(pullParser.getName())) {
                        orderGood.setOrderGoodsGuid(pullParser.nextText());
                    }
                    if ("PduId".equals(pullParser.getName())) {
                        orderGood.setPduId(pullParser.nextText());
                    }
                    if ("PduGuid".equals(pullParser.getName())) {
                        orderGood.setPduGuid(pullParser.nextText());
                    }
                    if ("PduName".equals(pullParser.getName())) {
                        orderGood.setPduName(pullParser.nextText());
                    }
                    if ("PduLogoUrl".equals(pullParser.getName())) {
                        orderGood.setPduLogoUrl(pullParser.nextText());
                    }
                    if ("TradePrice".equals(pullParser.getName())) {
                        orderGood.setTradePrice(pullParser.nextText());
                    }
                    if ("TradeNum".equals(pullParser.getName())) {
                        orderGood.setTradeNum(pullParser.nextText());
                    }
                    if ("Unit".equals(pullParser.getName())) {
                        orderGood.setUnit(pullParser.nextText());
                    }
                    if ("Price".equals(pullParser.getName())) {
                        orderGood.setPrice(pullParser.nextText());
                    }
                    if ("OrderId".equals(pullParser.getName())) {
                        orderGood.setOrderId(pullParser.nextText());
                    }

                    break;
                case XmlPullParser.END_TAG:
                    if ("EPdu".equals(pullParser.getName())) {
                        orderGoods.add(orderGood);
                        orderGood = null;
                    }
                    break;
            }
            event = pullParser.next();
        }

        return orderGoods;
    }


}
