package com.ninethree.playchannel;

import android.app.Application;

import com.google.gson.Gson;
import com.ninethree.playchannel.util.Constants;
import com.ninethree.playchannel.util.Log;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by user on 2016/7/11.
 */
public class MyApp extends Application {

    public static IWXAPI api;

    public static Gson gson;

    public static RequestQueue requestQueue;

    public static SendAuth.Resp resp;

    public static boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();

        gson = new Gson();

        ZXingLibrary.initDisplayOpinion(this);//初始化二维码扫描

        NoHttp.initialize(this);

        Logger.setTag("EWork");
        Logger.setDebug(Log.DEBUG);// 开始NoHttp的调试模式, 这样就能看到请求过程和日志

        //通过WXAPIFactory,获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID,true);
        //将应用的appid注册到微信
        api.registerApp(Constants.APP_ID);

        requestQueue = NoHttp.newRequestQueue();

    }

    public static Gson getGson() {
        return gson;
    }

    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
