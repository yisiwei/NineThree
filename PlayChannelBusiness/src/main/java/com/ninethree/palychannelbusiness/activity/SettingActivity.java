package com.ninethree.palychannelbusiness.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.Terminal.TerminalActivity;
import com.ninethree.palychannelbusiness.bean.SessionInfo;
import com.ninethree.palychannelbusiness.bean.Upgrade;
import com.ninethree.palychannelbusiness.service.DownLoadService;
import com.ninethree.palychannelbusiness.util.AppUtil;
import com.ninethree.palychannelbusiness.util.FileUtils;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.util.PackageUtil;
import com.ninethree.palychannelbusiness.view.MyDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

public class SettingActivity extends BaseActivity {

    private TextView mTerminaManagerBtn;
    private TextView mBindTicketMacBtn;

    private TextView mVersionBtn;
    private TextView mClearCacheBtn;

    private Button mLogoutBtn;

    private SessionInfo mSessionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("设置");

        initView();

        mSessionInfo = (SessionInfo) getIntent().getSerializableExtra("SessionInfo");

        // 设置当前版本号
        mVersionBtn.setText(PackageUtil.getVersionName(this));

        String cacheSize = FileUtils.formatFileSize(FileUtils.getFileSize(Glide.getPhotoCacheDir(this)));
        //缓存大小
        mClearCacheBtn.setText(cacheSize);

        String cookies = AppUtil.getCookies("http://sj.m.93966.net/");

        if (cookies == null) {
            mLogoutBtn.setVisibility(View.GONE);
        }

    }

    private void initView() {
        mTerminaManagerBtn = (TextView) findViewById(R.id.termina_manager);
        mTerminaManagerBtn.setOnClickListener(this);

        mBindTicketMacBtn = (TextView) findViewById(R.id.bind_ticket_mac);
        mBindTicketMacBtn.setOnClickListener(this);

        mVersionBtn = (TextView) findViewById(R.id.version);
        mVersionBtn.setOnClickListener(this);

        mClearCacheBtn = (TextView) findViewById(R.id.clear_cache);
        mClearCacheBtn.setOnClickListener(this);

        mLogoutBtn = (Button) findViewById(R.id.logout_btn);
        mLogoutBtn.setOnClickListener(this);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_setting);
    }

    public void openJoin(View v) {
        Intent intent = new Intent(this, WebViewActivity.class);
        String url = "http://ylc.93966.net/Org/Join/2";
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.termina_manager:
                if (mSessionInfo != null && mSessionInfo.getReturnCode() == 0) {
                    Intent intent = new Intent(getApplicationContext(), TerminalActivity.class);
                    intent.putExtra("user", mSessionInfo.getReturnObject().getUserBasic());
                    intent.putExtra("org", mSessionInfo.getReturnObject().getOrg());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
                break;
            case R.id.bind_ticket_mac://绑定售票机
                if (mSessionInfo != null && mSessionInfo.getReturnCode() == 0) {

                    verifyPermissions();

                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
                break;
            case R.id.version:
                upgrade();
                break;
            case R.id.clear_cache:
                clearCache();
                break;
            case R.id.logout_btn:

                MyDialog.show(this, "确定退出登录吗？", new MyDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick() {
                        logout();
                    }
                });

                break;
        }
    }

    private void startCapture() {
        Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
        intent.putExtra("user", mSessionInfo.getReturnObject().getUserBasic());
        intent.putExtra("org", mSessionInfo.getReturnObject().getOrg());
        startActivity(intent);
    }

    public void verifyPermissions() {
        // 检查权限
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    101);
        } else {
            startCapture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCapture();
                }
                break;
        }
    }

    //退出
    private void logout() {
        AppUtil.removeCookies();

        finish();
    }

    private void clearCache() {
        // 清理Webview缓存数据库
        //deleteDatabase("webview.db");
        //deleteDatabase("webviewCookiesChromium.db");
        //deleteDatabase("webviewCookiesChromiumPrivate.db");

        //FileUtils.deleteFile(getCacheDir());

        Glide.get(this).clearMemory();

        new ClearCacheTask().execute();
    }

    private class ClearCacheTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            Glide.get(getApplicationContext()).clearDiskCache();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            toast("清理成功");
            String cacheSize = FileUtils.formatFileSize(FileUtils.getFileSize(Glide.getPhotoCacheDir(getApplicationContext())));
            //缓存大小
            mClearCacheBtn.setText(cacheSize);
        }
    }

    //检查新版本
    private void upgrade() {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest("http://m.93966.net:1210/AndroidDown/ylqyj_upgrade.json");

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
                if (code == 200 || code == 304) {
                    Upgrade upgrade = MyApp.getGson().fromJson(response.get().toString(), Upgrade.class);
                    if (PackageUtil.getVersionCode(getApplicationContext()) < upgrade
                            .getCode()) {
                        showUpgradeDialog(upgrade.getUrl());
                    } else {
                        toast("已是最新版本");
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Log.i("onFailed:" + response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                mProgressDialog.dismiss();
            }
        });

    }

    //显示有新版本提示对话框
    private void showUpgradeDialog(final String url) {
        MyDialog.show(this, "检测到新版本", "立即更新", new MyDialog.OnConfirmListener() {

            @Override
            public void onConfirmClick() {
                downLoad(url);
            }
        });
    }

    //下载新版本
    private void downLoad(String url) {
        Log.i("下载Url:" + url);
        Intent intent = new Intent(this, DownLoadService.class);
        intent.putExtra("downloadUrl", url);
        startService(intent);
    }


}
