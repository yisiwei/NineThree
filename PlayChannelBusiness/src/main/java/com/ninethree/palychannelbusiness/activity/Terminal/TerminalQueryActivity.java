package com.ninethree.palychannelbusiness.activity.Terminal;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.Terminal;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.CodeUtil;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.view.CircleImageView;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerminalQueryActivity extends BaseActivity {

    private TextView mCodeTv;
    private ImageView mQRCodeImg;

    private LinearLayout mResultLayout;
    private LinearLayout mCodetLayout;

    private CircleImageView mHeadImg;
    private TextView mTermina;
    private TextView mOrgName;
    private TextView mPduName;
    private TextView mStatus;//状态
    private TextView mUpdateTime;
    private TextView mEditTime;
    private TextView mUsername;

    private User mUser;
    private Org mOrg;
    private String mCommandCode;

    private String mTerminalCodeId;

    private String isRunning = "Running";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        mUser = (User) getIntent().getSerializableExtra("user");
        mOrg = (Org) getIntent().getSerializableExtra("org");
        mCommandCode = getIntent().getStringExtra("commandCode");
        String title = getIntent().getStringExtra("type");

        mTitle.setText(title);

        new EditTask().execute(mCommandCode,mUser.getUserID(),mUser.getUserGuid(),mOrg.getOrgId(),mOrg.getOrgGuid());

    }

    private void initView() {
        mResultLayout = (LinearLayout) findViewById(R.id.ll_result);
        mCodetLayout = (LinearLayout) findViewById(R.id.ll_code);

        mHeadImg = (CircleImageView) findViewById(R.id.headImg);
        mTermina = (TextView) findViewById(R.id.termina_code);
        mOrgName = (TextView) findViewById(R.id.orgName);
        mPduName = (TextView) findViewById(R.id.pduName);
        mStatus = (TextView) findViewById(R.id.status);
        mUpdateTime = (TextView) findViewById(R.id.update_time);
        mEditTime = (TextView) findViewById(R.id.edit_time);
        mUsername = (TextView) findViewById(R.id.username);

        mCodeTv = (TextView) findViewById(R.id.code);
        mQRCodeImg = (ImageView) findViewById(R.id.qr_code);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_terminal_add);
    }

    @Override
    public void onClick(View view) {

    }

    private class EditTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        /**
         * @PduId                    bigint,
         * @PduGuid                  uniqueidentifier,
         * @UserId                   bigint,
         * @UserGuid                 uniqueidentifier
         */
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("CommandCode", params[0]);
            map.put("UserId", params[1]);
            map.put("UserGuid", params[2]);
            map.put("UserOrgId", params[3]);
            map.put("UserOrgGuid", params[4]);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "Terminal_Edit_501_1", map);
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
                    success(dataStr);
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
            String scanCode = tableArray.optJSONObject(0).getString("ScanCode");

            Bitmap bitmap = CodeUtil.createQRImage(scanCode, 500, 500);
            mQRCodeImg.setImageBitmap(bitmap);
            mCodeTv.setText(scanCode);

            mTerminalCodeId = tableArray.optJSONObject(0).getString("TerminalCodeId");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new IsOverTask().execute(mTerminalCodeId);
                }
            },2000);

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    private class IsOverTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * @PduId                    bigint,
         * @PduGuid                  uniqueidentifier,
         * @UserId                   bigint,
         * @UserGuid                 uniqueidentifier
         */
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("TerminalCodeId", params[0]);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "TerminalCode_Query_ByScanOver", map);
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
            if (null != result) {
                if(isRunning == null){
                    Log.i("停止");
                    return;
                }
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
            int isOver = tableArray.optJSONObject(0).getInt("IsOver");
            int siSucceed = tableArray.optJSONObject(0).getInt("IsSucceed");

            if (isOver == 1){
                if(siSucceed == 1) {
                    //显示
                    JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
                    TypeToken<List<Terminal>> typeToken1 = new TypeToken<List<Terminal>>() {
                    };
                    List<Terminal> terminas = MyApp.getGson().fromJson(tableArray1.toString(),
                            typeToken1.getType());
                    Terminal termina = terminas.get(0);

                    mTermina.setText("设备："+termina.getTerminalNum());
                    mOrgName.setText("商家："+termina.getOrgName());
                    mPduName.setText("产品："+termina.getPduName());
                    mStatus.setText("状态："+ termina.getDelFlag());
                    mUpdateTime.setText("访问时间："+termina.getUpdateTime());
                    mEditTime.setText("配置时间："+termina.getEditTime());
                    mUsername.setText("配置者："+termina.getUserName());

                    Glide.with(this)
                            .load(termina.getPhoto())
                            .centerCrop()
                            //.crossFade()
                            .dontAnimate()
                            .placeholder(R.drawable.default_head)
                            .into(mHeadImg);

                    mResultLayout.setVisibility(View.VISIBLE);//显示扫描结果
                    mCodetLayout.setVisibility(View.GONE);//隐藏二维码
                } else {
                    String msg = tableArray.optJSONObject(0).getString("IsSucceed");
                    toast(msg);
                }

            }else{
                //调用自己
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new IsOverTask().execute(mTerminalCodeId);
                    }
                },2000);
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = null;
    }
}
