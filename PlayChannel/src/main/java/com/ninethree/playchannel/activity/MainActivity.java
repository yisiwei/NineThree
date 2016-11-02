package com.ninethree.playchannel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.ninethree.playchannel.MyApp;
import com.ninethree.playchannel.R;
import com.ninethree.playchannel.adapter.ImagePagerAdapter;
import com.ninethree.playchannel.bean.SessionInfo;
import com.ninethree.playchannel.bean.Upgrade;
import com.ninethree.playchannel.service.DownLoadService;
import com.ninethree.playchannel.util.AppUtil;
import com.ninethree.playchannel.util.FileUtils;
import com.ninethree.playchannel.util.ListUtils;
import com.ninethree.playchannel.util.Log;
import com.ninethree.playchannel.util.PackageUtil;
import com.ninethree.playchannel.view.AutoScrollViewPager;
import com.ninethree.playchannel.view.MyDialog;
import com.ninethree.playchannel.webservice.DBPubService;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    //轮播
    private ArrayList<Integer> imgList; // 轮播图片List
    private ArrayList<ImageView> portImg;// 轮播圆点List
    private LinearLayout dotsLayout = null;// 圆点布局
    private int preSelImgIndex = 0;
    private AutoScrollViewPager viewPager;// 轮播ViewPager

    //按钮
    private Button mScanBtn;//任性刷
    private Button mMyFootprintBtn;//游玩足迹
    private Button mNearbyPlayBtn;//附近游乐场
    private Button mPlayChannelBtn;//游乐频道

    private Button mMyCardBtn;//我的卡券
    private Button mMyPduBtn;//我的产品
    private Button mMyRecordBtn;//游玩记录
    private Button mUserCenterBtn;//用户中心

    private Button mPromotionBtn;//优惠促销
    private Button mActivityBtn;//热门活动
    private Button mTouristBtn;//最美游客
    private Button mKursaalBtn;//最美游乐场

    private SessionInfo mSessionInfo;

    private String mCookiesValue;

    private String mUrl;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        mLeftBtn.setImageResource(R.drawable.icon_setting);
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clearCache();
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
            }
        });
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setImageResource(R.drawable.icon_scan);
        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPermissions();
            }
        });

        initView();
        initEvent();

        // 轮播广告图
        dotsLayout = (LinearLayout) findViewById(R.id.home_navig_dots);
        imgList = new ArrayList<Integer>();
        imgList.add(R.drawable.banner_1);
        imgList.add(R.drawable.banner_2);
        imgList.add(R.drawable.banner_3);
        imgList.add(R.drawable.banner_4);

        // 初始化轮播圆点
        InitFocusIndicatorContainer();

        viewPager.setAdapter(new ImagePagerAdapter(this, imgList)
                .setInfiniteLoop(true));
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 轮播间隔时间
        viewPager.setInterval(3000);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % ListUtils.getSize(imgList));

        upgrade();

    }

    private void startCapture(){
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        //startActivityForResult(intent, 1000);
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
        }else {
            startCapture();
        }
    }

    /**
     * ViewPager监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {

            position = position % imgList.size();
            // 修改上一次选中项的背景
            portImg.get(preSelImgIndex).setImageResource(R.drawable.doc_unshow);
            // 修改当前选中项的背景
            portImg.get(position).setImageResource(R.drawable.doc_show);
            preSelImgIndex = position;

        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }

    /**
     * 轮播圆点
     */
    private void InitFocusIndicatorContainer() {
        portImg = new ArrayList<ImageView>();
        for (int i = 0; i < imgList.size(); i++) {
            ImageView localImageView = new ImageView(this);
            localImageView.setId(i);
            ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
            localImageView.setScaleType(localScaleType);
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
                    36, 36);
            localImageView.setLayoutParams(localLayoutParams);
            localImageView.setPadding(10, 10, 10, 10);
            localImageView.setImageResource(R.drawable.doc_unshow);
            portImg.add(localImageView);
            dotsLayout.addView(localImageView);
        }
    }

    private void initView() {
        // 轮播广告图
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);

        mPlayChannelBtn = (Button) findViewById(R.id.play_channer);
        mNearbyPlayBtn = (Button) findViewById(R.id.nearby_play);
        mMyFootprintBtn = (Button) findViewById(R.id.my_footprint);

        mScanBtn = (Button) findViewById(R.id.scan);

        mMyPduBtn = (Button) findViewById(R.id.my_pdu);
        mMyCardBtn = (Button) findViewById(R.id.my_card);
        mUserCenterBtn = (Button) findViewById(R.id.user_center);

        mPromotionBtn = (Button) findViewById(R.id.promotion);
        mActivityBtn = (Button) findViewById(R.id.activity);

        mTouristBtn = (Button) findViewById(R.id.tourist);
        mKursaalBtn = (Button) findViewById(R.id.kursaal);

        mMyRecordBtn = (Button) findViewById(R.id.my_record);
    }

    private void initEvent() {
        mPlayChannelBtn.setOnClickListener(this);
        mNearbyPlayBtn.setOnClickListener(this);
        mMyFootprintBtn.setOnClickListener(this);
        mScanBtn.setOnClickListener(this);

        mMyPduBtn.setOnClickListener(this);
        mMyCardBtn.setOnClickListener(this);
        mUserCenterBtn.setOnClickListener(this);

        mPromotionBtn.setOnClickListener(this);
        mActivityBtn.setOnClickListener(this);
        mTouristBtn.setOnClickListener(this);
        mKursaalBtn.setOnClickListener(this);

        //游玩记录
        mMyRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSessionInfo != null && mSessionInfo.getReturnCode() == 0){
                    Intent intent = new Intent(getApplicationContext(),MyRecordActivity.class);
                    intent.putExtra("user",mSessionInfo.getReturnObject().getUserBasic());
                    startActivity(intent);
                }else{
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.startAutoScroll();

        String cookies = AppUtil.getCookies("http://shop.93966.net/h5user/card/mycard");

        if (cookies != null){
            if (mCookiesValue == null || !cookies.equals(mCookiesValue)){
                mCookiesValue = cookies;
                String[] arr = cookies.split("=");
                Log.i("value:"+arr[1]);
                new SessionTask().execute(arr[1]);
            }
        }else{
            mCookiesValue = null;
            mSessionInfo = null;
            mTitle.setText(R.string.app_name);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WebViewActivity.class);
        String url = null;
        switch (v.getId()) {
            case R.id.play_channer:
                url = "http://ylc.93966.net";
                break;
            case R.id.nearby_play:
                url = "http://ylc.93966.net/playground/store/nearby";
                break;
            case R.id.my_footprint:
                url = "http://ylc.93966.net/playground/my/collect";
                break;
            case R.id.scan://任性刷
                url = "http://pay.93966.net/ScanCode";
                break;
            case R.id.my_pdu:
                url = "http://shop.93966.net/h5user/pdu/mypdu";
                break;
            case R.id.my_card:
                url = "http://shop.93966.net/h5user/card/mycard";
                break;
            case R.id.user_center://用户中心
                url = "http://shop.93966.net/h5user/UserCenter/Info";
                break;
            case R.id.promotion:
                url = "http://ylc.93966.net/playground/home/index#Yl_md6";
                break;
            case R.id.activity:
                url = "http://ylc.93966.net/Playground/Store/News";
                break;
            case R.id.tourist:
                url = "http://ylc.93966.net/Playground/Topic/List?id=9";
                break;
            case R.id.kursaal:
                url = "http://ylc.93966.net/Playground/Topic/List?id=8";
                break;
        }
        intent.putExtra("url", url);
        startActivity(intent);
    }


    private void upgrade() {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest("http://m.93966.net:1210/AndroidDown/ylpd_upgrade.json");

        MyApp.requestQueue.add(1000, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

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
                        //showUpgradeDialog(upgrade.getUrl());
                        mUrl = upgrade.getUrl();
                        showUpgradeDialog();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                Log.i("onFailed:" + exception.getMessage());
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }

    private void showUpgradeDialog() {
        MyDialog.show(this, "检测到新版本", "立即更新", new MyDialog.OnConfirmListener() {

            @Override
            public void onConfirmClick() {
                verifyStoragePermissions(MainActivity.this);
                //downLoad(url);
            }
        });
    }

    //下载新版本
    private void downLoad() {
        Log.i("下载Url:" + mUrl);
        Intent intent = new Intent(this, DownLoadService.class);
        intent.putExtra("downloadUrl", mUrl);
        startService(intent);
    }

    public void verifyStoragePermissions(Activity activity) {
        // 检查权限
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }else {
            downLoad();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downLoad();
                }
                break;
            case 101://相机
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCapture();
                }
                break;
        }
    }

    private class SessionTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = DBPubService.getSessionInfo(params[0]);
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

            Log.i("result:"+result);

            if (null != result) {
                success(result);
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);

            mSessionInfo = MyApp.getGson().fromJson(jsonObject.toString(),
                    SessionInfo.class);

            if (mSessionInfo.getReturnCode() == 0){
                mTitle.setText(mSessionInfo.getReturnObject().getUserBasic().getNickName());
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
//            if (requestCode == 1000) {
//                //处理扫描结果（在界面上显示）
//                if (null != data) {
//                    Bundle bundle = data.getExtras();
//                    if (bundle == null) {
//                        return;
//                    }
//                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                        String result = bundle.getString(CodeUtils.RESULT_STRING);
//
//                        Intent intent = new Intent(this, WebViewActivity.class);
//                        intent.putExtra("url", result);
//                        startActivity(intent);
//
//                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                        toast("解析二维码失败");
//                    }
//                }
//            }
        }
    }
}
