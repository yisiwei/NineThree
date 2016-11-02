package com.ninethree.playchannel.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * @ClassName: WebServiceUtil
 * @Description:WebService工具类
 */
public class WebServiceUtil {

    private static final String NameSpace = "http://m.93966.net:1210";

    private static final int TIME_OUT = 10 * 1000;//超时时间

    public static SoapObject loadResult(String url, String soapAction, String methodName,
                                        String param) {

        // SoapObject类的第1个参数表示WebService的命名空间，可以从WSDL文档中找到WebService的命名空间。
        // 第2个参数表示要调用的WebService方法名。
        SoapObject soapObject = new SoapObject(NameSpace, methodName);

        // addProperty方法用于设置参数
        soapObject.addProperty("str", param);

        // 创建SoapSerializationEnvelope对象时需要通过SoapSerializationEnvelope类的构造方法设置SOAP协
        // 议的版本号。
        // 该版本号需要根据服务端WebService的版本号设置。在创建SoapSerializationEnvelope对象后，
        // 不要忘了设置 SoapSerializationEnvelope类的bodyOut属性，该属性的值就创建的SoapObject对象。
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // 版本
        envelope.bodyOut = soapObject;

        // 设置与.NET提供的Web service保持有良好的兼容性
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        // 创建HttpTransportSE对象。通过HttpTransportSE类的构造方法可以指定WebService的WSDL文档的URL
        HttpTransportSE trans = new HttpTransportSE(url, TIME_OUT);
        trans.debug = true; // 使用调试功能

        try {
            // 使用call方法调用WebService方法
            trans.call(soapAction, envelope);
            Log.i("Call Successful!");
        } catch (IOException e) {
            Log.i("IOException");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            Log.i("XmlPullParserException");
            e.printStackTrace();
        }

        SoapObject result = (SoapObject) envelope.bodyIn;
        // SoapObject result = (SoapObject) envelope.getResponse();

        return result;
    }

}
