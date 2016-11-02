package com.ninethree.playchannel.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.playchannel.MyApp;
import com.ninethree.playchannel.R;
import com.ninethree.playchannel.bean.Upgrade;
import com.ninethree.playchannel.service.DownLoadService;
import com.ninethree.playchannel.util.AppUtil;
import com.ninethree.playchannel.util.FileUtils;
import com.ninethree.playchannel.util.Log;
import com.ninethree.playchannel.util.PackageUtil;
import com.ninethree.playchannel.view.MyDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

public class SettingActivity extends BaseActivity {

    private TextView mVersionBtn;
    private TextView mClearCacheBtn;

    private Button mLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("设置");

        initView();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

    //退出
    private void logout() {
        AppUtil.removeCookies();

        finish();
    }

    private void clearCache(){
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
        Request<JSONObject> request = NoHttp.createJsonObjectRequest("http://m.93966.net:1210/AndroidDown/ylpd_upgrade.json");

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
                    }else {
                        toast("已是最新版本");
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                Log.i("onFailed:" + exception.getMessage());
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
