package com.ninethree.palychannelbusiness.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.bean.WxToken;
import com.ninethree.palychannelbusiness.bean.WxUserInfo;
import com.ninethree.palychannelbusiness.util.Constants;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.view.MyProgressDialog;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
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
        Log.i("onResp>>>"+baseResp.errCode+",baseResp.getType:"+baseResp.getType());

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK://成功

                if(baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH){//授权登录
                    //((SendAuth.Resp) baseResp).token
                    MyApp.resp = (SendAuth.Resp) baseResp;
                    MyApp.isLogin = true;
                }else if(baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
                    toast("分享成功");
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://取消
                if(baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
                    toast("分享取消");
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://拒绝
                break;
            default:
                break;
        }

//        bundle = getIntent().getExtras();
//        SendAuth.Resp resp = new SendAuth.Resp(bundle);
//        //获取到code之后，需要调用接口获取到access_token
//        Log.i("resp.errCode:"+resp.errCode);
//        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
//            String code = resp.token;
//            MyApp.resp = resp;
//            MyApp.isLogin = true;
//            Log.i("code="+code);
//        }else if(resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){//取消
//            toast("取消");
//        }else if(resp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED){//拒绝
//            toast("拒绝");
//        }
        finish();
    }

    private void toast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}