package com.ninethree.playchannel.wxapi;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ninethree.playchannel.MyApp;
import com.ninethree.playchannel.bean.WxToken;
import com.ninethree.playchannel.bean.WxUserInfo;
import com.ninethree.playchannel.util.Constants;
import com.ninethree.playchannel.util.Log;
import com.ninethree.playchannel.view.MyProgressDialog;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private Bundle bundle;
    private MyProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("WXEntryActivity...onCreate");

        dialog = new MyProgressDialog(this);

        MyApp.api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.i("onReq---");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("onNewIntent---");
        super.onNewIntent(intent);
        setIntent(intent);
        MyApp.api.handleIntent(intent, this);//必须调用此句话
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("onResp---");
        bundle = getIntent().getExtras();
        SendAuth.Resp resp = new SendAuth.Resp(bundle);
        //获取到code之后，需要调用接口获取到access_token
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            String code = resp.token;
            MyApp.resp = resp;
            MyApp.isLogin = true;
            Log.i("code="+code);
            //getToken(code);
//            if(BaseApplication. isWxLogin){
//
//            } else{
//                WXEntryActivity. this.finish();
//            }
        }
        finish();
    }

    //这个方法会取得accesstoken  和openID
    private void getToken(String code){

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ Constants.APP_ID+"&secret="+Constants.APP_SECRET+"&code=" +code+"&grant_type=authorization_code";

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url);

        MyApp.getRequestQueue().add(1000, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                dialog.show();
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                int code = response.getHeaders().getResponseCode();
                Log.i("success-ResponseCode:" + code);
                Log.i("success--get:" + response.get());
                if (code == 200) {
                    WxToken token = MyApp.getGson().fromJson(response.get().toString(), WxToken.class);
                    getUserInfo(token.getAccess_token(),token.getOpenid());
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                Log.i("onFailed:" + exception.getMessage());
                Toast.makeText(getApplicationContext(),"授权失败",Toast.LENGTH_SHORT).show();
                WXEntryActivity.this.finish();
            }

            @Override
            public void onFinish(int what) {
                dialog.dismiss();
            }
        });

    }

    //获取到token和openID之后，调用此接口得到身份信息
    private void getUserInfo(String token,String openID){

        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +token+"&openid=" +openID;

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url);

        MyApp.getRequestQueue().add(1000, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                dialog.show();
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                int code = response.getHeaders().getResponseCode();
                Log.i("success-ResponseCode:" + code);
                Log.i("success--get:" + response.get());
                if (code == 200) {
                    WxUserInfo userInfo = MyApp.getGson().fromJson(response.get().toString(), WxUserInfo.class);
                    Intent data = new Intent();
                    data.putExtra("wxUserInfo",userInfo);
                    setResult(RESULT_OK,data);
                    finish();
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                Log.i("onFailed:" + exception.getMessage());
                Toast.makeText(getApplicationContext(),"授权失败",Toast.LENGTH_SHORT).show();
                WXEntryActivity.this.finish();
            }

            @Override
            public void onFinish(int what) {
                dialog.dismiss();
            }
        });
    }
}