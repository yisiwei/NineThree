package com.ninethree.palychannelbusiness.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.AscPackIntResult;
import com.ninethree.palychannelbusiness.bean.AscPackResult;
import com.ninethree.palychannelbusiness.bean.Result;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.bean.WxToken;
import com.ninethree.palychannelbusiness.bean.WxUserInfo;
import com.ninethree.palychannelbusiness.util.AppUtil;
import com.ninethree.palychannelbusiness.util.ByteUtil;
import com.ninethree.palychannelbusiness.util.Constants;
import com.ninethree.palychannelbusiness.util.GZipUtil;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.util.Md5Util;
import com.ninethree.palychannelbusiness.util.PreferencesUtil;
import com.ninethree.palychannelbusiness.util.RegexUtil;
import com.ninethree.palychannelbusiness.util.StringUtil;
import com.ninethree.palychannelbusiness.webservice.DBPubService;
import com.ninethree.palychannelbusiness.webservice.LoginService;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;

    private ImageView mLoginWxBtn;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle.setText(R.string.login);

        initView();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_login);
    }

    private void initView() {
        mUsernameEt = (EditText) findViewById(R.id.username);
        mPasswordEt = (EditText) findViewById(R.id.password);

        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(this);

        mLoginWxBtn = (ImageView) findViewById(R.id.login_wx);
        mLoginWxBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login://登录
                String username = mUsernameEt.getText().toString();
                String password = mPasswordEt.getText().toString();

                if (StringUtil.isNullOrEmpty(username)) {
                    toast("请输入手机号");
                    return;
                }

                if (!RegexUtil.isMobile(username)) {
                    toast("请输入正确的手机号");
                    return;
                }

                if (StringUtil.isNullOrEmpty(password)) {
                    toast("请输入密码");
                    return;
                }

                new LoginTask().execute(username, Md5Util.md5(password));

                break;
            case R.id.login_wx://微信登录

                if (!MyApp.api.isWXAppInstalled()) {
                    toast("您尚未安装微信");
                    return;
                }

                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "ylqyj_wx_login";
                MyApp.api.sendReq(req);
                Log.i("开始调用");

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApp.resp != null) {
            if (MyApp.resp.getType() == ConstantsAPI.COMMAND_SENDAUTH && MyApp.isLogin) {
                Log.i("token:" + MyApp.resp.token);
                getToken(MyApp.resp.token);
                MyApp.isLogin = false;
            }
        }
    }

    /**
     * 登录异步请求
     */
    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String param = LoginService.loginParamPack(getApplicationContext(),
                    params[0], params[1]);
            SoapObject result = LoginService.login(param);
            String code = null;
            try {
                code = result.getProperty(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            if (null != result) {
                // Base64转byte[]
                byte[] resultByte = Base64.decode(result);
                // 解Guid
                AscPackResult results = ByteUtil.ascPackDownGuid(resultByte);
                byte[] secBytes = (byte[]) results.getResultValue();
                String secBase64 = Base64.encode(secBytes);
                // 将密钥存入SharedPreferences
                PreferencesUtil.putString(getApplicationContext(), "userSec",
                        secBase64);

                // 解true/false
                results = ByteUtil.ascPackDownBoolean(results.getResultBytes());
                boolean bool = (boolean) results.getResultValue();
                Log.i("bool:" + bool);
                if (bool) {// 请求成功
                    // 拆包byte[]
                    AscPackIntResult byteLengthResult = ByteUtil
                            .ascPackDownBytes(results.getResultBytes());
                    Log.i("byte[]长度:" + byteLengthResult.resultValue);
                    // 解压byte[]
                    byte[] dataByte = GZipUtil
                            .unGZip(byteLengthResult.resultBytes);
                    // byte转String
                    String dataStr = ByteUtil.byteToString(dataByte);
                    Log.i("dataStr:" + dataStr);
                    success(dataStr);

                } else {// 请求失败
                    results = ByteUtil.ascPackDownString(results
                            .getResultBytes());
                    String resultData = results.getResultValue().toString();
                    Log.i("false_resultJson:" + resultData);
                    try {
                        JSONObject jsonObject = new JSONObject(resultData);
                        String msg = jsonObject.optString("msg");
                        toast(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray tableArray = jsonObject.optJSONArray("Table");
            TypeToken<List<Result>> typeToken = new TypeToken<List<Result>>() {
            };
            List<Result> results = MyApp.getGson().fromJson(
                    tableArray.toString(), typeToken.getType());
            Log.i("results:" + results.get(0));
            Result result = results.get(0);

            if (result.getResultNum() == 0) {// 登录成功

                // user
                JSONArray table6Array = jsonObject.optJSONArray("Table6");
                TypeToken<List<User>> userTypeToken = new TypeToken<List<User>>() {
                };
                List<User> users = MyApp.getGson().fromJson(
                        table6Array.toString(), userTypeToken.getType());

                mUser = users.get(0);

                new LoginGuidTask().execute(mUser.getUserGuid().replace("-", "") + mUser.getUserID());

            } else {
                toast(result.getRetMsg());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class LoginGuidTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("params:" + params[0]);
            SoapObject result = DBPubService.getRetLoginGuid(params[0]);
            String code = null;
            try {
                code = result.getProperty(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            Log.i("result:" + result);

            if (null != result) {
                AppUtil.setCookies(getApplicationContext(), "http://sj.m.93966.net/", "ULSID=" + result + mUser.getUserID() + ";Max-Age=2592000;Domain=.93966.net;Path=/");

                finish();
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    //这个方法会取得accesstoken  和openID
    private void getToken(String code) {

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constants.APP_ID + "&secret=" + Constants.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url);

        MyApp.requestQueue.add(1000, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                mProgressDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                int code = response.getHeaders().getResponseCode();
                Log.i("success-ResponseCode:" + code);
                Log.i("success--get:" + response.get());
                if (code == 200) {
                    WxToken token = MyApp.getGson().fromJson(response.get().toString(), WxToken.class);
                    getUserInfo(token.getAccess_token(), token.getOpenid());
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Log.i("onFailed:" + response.getException().getMessage());
                Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(int what) {
                mProgressDialog.dismiss();
            }
        });

    }

    //获取到token和openID之后，调用此接口得到身份信息
    private void getUserInfo(String token, String openID) {

        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openID;

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url);

        MyApp.requestQueue.add(1000, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                mProgressDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                int code = response.getHeaders().getResponseCode();
                Log.i("success-ResponseCode:" + code);
                Log.i("success--get:" + response.get());
                if (code == 200) {
                    WxUserInfo userInfo = MyApp.getGson().fromJson(response.get().toString(), WxUserInfo.class);
                    Log.i("" + userInfo.toString());

                    new WeiXinLoginTask().execute(userInfo.getNickname(), userInfo.getSex() + "", userInfo.getUnionid(), userInfo.getHeadimgurl());
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Log.i("onFailed:" + response.getException().getMessage());
                Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(int what) {
                mProgressDialog.dismiss();
            }
        });
    }

    private class WeiXinLoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("NickName", params[0]);
            map.put("Sex", params[1]);
            map.put("Unionid", "" + params[2]);
            map.put("Photo", "" + params[3]);
            map.put("Openid", "");
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.User.Basic", "An_UserBasic_Insert_Weixin", map);
            SoapObject result = DBPubService.getPubDB(getApplicationContext(),
                    param);
            String code = null;
            try {
                code = result.getProperty(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            if (null != result) {
                String dataStr = DBPubService.ascPackDown(
                        getApplicationContext(), result);
                if (dataStr != null) {
                    success1(dataStr);
                }
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success1(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);

            JSONArray tableArray = jsonObject.optJSONArray("Table");

            TypeToken<List<Result>> typeToken = new TypeToken<List<Result>>() {
            };

            List<Result> results = MyApp.getGson().fromJson(tableArray.toString(),
                    typeToken.getType());

            Result result = results.get(0);

            if (result.getResultNum() == 0) {

                JSONArray tableArray1 = jsonObject.optJSONArray("Table1");

                TypeToken<List<User>> typeToken1 = new TypeToken<List<User>>() {
                };

                List<User> users = MyApp.getGson().fromJson(tableArray1.toString(),
                        typeToken1.getType());

                mUser = users.get(0);

                new LoginGuidTask().execute(mUser.getUserGuid().replace("-", "") + mUser.getUserID());

            } else {
                toast(result.getRetMsg());
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

}
