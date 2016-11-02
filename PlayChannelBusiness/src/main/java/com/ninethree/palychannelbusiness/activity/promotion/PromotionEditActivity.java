package com.ninethree.palychannelbusiness.activity.promotion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.Product;
import com.ninethree.palychannelbusiness.bean.Promotion;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.DateUtil;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.util.StringUtil;
import com.ninethree.palychannelbusiness.view.MyDateDialog;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

/**
 * 编辑促销
 */
public class PromotionEditActivity extends BaseActivity {

    private EditText mActivityNameEt;
    private EditText mActivityContentEt;
    private TextView mActivityType;
    private EditText mNumberEt;
    private TextView mPriceTv;

    private LinearLayout mPduLayout;
    private TextView mPduNameTv;
    private ImageView mPduImg;

    private Button mAcStartDate;
    private Button mAcEndDate;
    private Button mValidStartDate;
    private Button mValidEndDate;

    private EditText mShareTitle;
    private EditText mShareContent;
    private ImageView mShareImg;
    private Button mChooseImgBtn;

    private Product mProduct;

    private Promotion mPromotion;
    private Org mOrg;
    private User mUser;

    private int type = 0;//0=面值卡，1=产品

    private String mShareImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("编辑促销");
        mRightBtn.setImageResource(R.drawable.icon_save);
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setOnClickListener(this);

        initView();

        mOrg = (Org) getIntent().getSerializableExtra("org");
        mUser = (User) getIntent().getSerializableExtra("user");
        mPromotion = (Promotion) getIntent().getSerializableExtra("promotion");


        if (mPromotion.getPromotionType() == 1) {//产品
            mPduNameTv.setText(mPromotion.getDataTitle());
            Glide.with(this)
                    .load(mPromotion.getDataLogo())
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mPduImg);
            mPduLayout.setVisibility(View.VISIBLE);
        }

        mActivityNameEt.setText(mPromotion.getTitle());
        mActivityContentEt.setText(mPromotion.getNote());
        mActivityType.setText(mPromotion.getPromotionType() == 0 ? "面值卡" : "产品");

        mPriceTv.setText(mPromotion.getPrice() + "元");

        mNumberEt.setText(mPromotion.getIssueNum() + "");
        mAcStartDate.setText(mPromotion.getPromotionBeginDate().substring(0, 10));
        mAcEndDate.setText(mPromotion.getPromotionEndDate().substring(0, 10));
        mValidStartDate.setText(mPromotion.getUseBeginDate().substring(0, 10));
        mValidEndDate.setText(mPromotion.getUseEndDate().substring(0, 10));

        mShareTitle.setText(mPromotion.getShareTitle());
        mShareContent.setText(mPromotion.getShareNote());

        mShareImgUrl = mPromotion.getShareImgUrl();
        Glide.with(this)
                .load(mPromotion.getShareImgUrl())
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(mShareImg);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_promotion_edit);
    }

    private void initView() {

        mActivityNameEt = (EditText) findViewById(R.id.activity_name);
        mActivityContentEt = (EditText) findViewById(R.id.activity_content);
        mActivityType = (TextView) findViewById(R.id.type);
        mPriceTv = (TextView) findViewById(R.id.price);
        mNumberEt = (EditText) findViewById(R.id.number);

        mPduLayout = (LinearLayout) findViewById(R.id.ll_pdu);
        mPduNameTv = (TextView) findViewById(R.id.pduName);
        mPduImg = (ImageView) findViewById(R.id.pduImg);

        mAcStartDate = (Button) findViewById(R.id.activity_start_date);
        mAcStartDate.setOnClickListener(this);
        mAcEndDate = (Button) findViewById(R.id.activity_end_date);
        mAcEndDate.setOnClickListener(this);
        mValidStartDate = (Button) findViewById(R.id.valid_start_date);
        mValidStartDate.setOnClickListener(this);
        mValidEndDate = (Button) findViewById(R.id.valid_end_date);
        mValidEndDate.setOnClickListener(this);

        mShareTitle = (EditText) findViewById(R.id.share_title);
        mShareContent = (EditText) findViewById(R.id.share_content);
        mShareImg = (ImageView) findViewById(R.id.share_img);
        mChooseImgBtn = (Button) findViewById(R.id.share_img_btn);
        mChooseImgBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_start_date:
                MyDateDialog.show(this, new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String endDate = mAcEndDate.getText().toString();
                        if (DateUtil.compareDate(date, endDate) > 0){
                            toast("开始日期不能大于结束日期");
                            return;
                        }
                        mAcStartDate.setText(date);
                    }
                });
                break;
            case R.id.activity_end_date:
                MyDateDialog.show(this, new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String beginDate = mAcStartDate.getText().toString();
                        if (DateUtil.compareDate(beginDate, date) > 0){
                            toast("结束日期不能小于开始日期");
                            return;
                        }
                        mAcEndDate.setText(date);
                    }
                });
                break;
            case R.id.valid_start_date:
                MyDateDialog.show(this, new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String endDate = mValidEndDate.getText().toString();
                        if (DateUtil.compareDate(date, endDate) > 0){
                            toast("开始日期不能大于结束日期");
                            return;
                        }
                        mValidStartDate.setText(date);
                    }
                });
                break;
            case R.id.valid_end_date:
                MyDateDialog.show(this, new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String beginDate = mValidStartDate.getText().toString();
                        if (DateUtil.compareDate(beginDate, date) > 0){
                            toast("结束日期不能小于开始日期");
                            return;
                        }
                        mValidEndDate.setText(date);
                    }
                });
                break;
            case R.id.share_img_btn://选择分享图片
                Intent intent = new Intent(this, ShareImgsActivity.class);
                startActivityForResult(intent, 2000);
                break;
            case R.id.title_right_btn://保存

                String activityName = mActivityNameEt.getText().toString();

                if (StringUtil.isNullOrEmpty(activityName)) {
                    toast("请输入活动名称");
                    return;
                }

                String activityContent = mActivityContentEt.getText().toString();

                String numberStr = mNumberEt.getText().toString();
                if (StringUtil.isNullOrEmpty(numberStr)) {
                    toast("请输入发行数量");
                    return;
                }
                int number = Integer.valueOf(numberStr);
                if (number < 1) {
                    toast("发行数量必须大于0");
                    return;
                }

                String acStartDate = mAcStartDate.getText().toString();
                String acEndDate = mAcEndDate.getText().toString();
                String validStartDate = mValidStartDate.getText().toString();
                String validEndDate = mValidEndDate.getText().toString();

                String shareTitle = mShareTitle.getText().toString();
                if (StringUtil.isNullOrEmpty(shareTitle)) {
                    toast("请输入分享标题");
                    return;
                }

                String shareContent = mShareContent.getText().toString();

                Promotion pro = new Promotion();

                pro.setPromotionId(mPromotion.getPromotionId());
                pro.setOrgId(Integer.valueOf(mOrg.getOrgId()));

                pro.setTitle(activityName);
                pro.setNote(activityContent);

                pro.setPromotionBeginDate(acStartDate);
                pro.setPromotionEndDate(acEndDate);
                pro.setUseBeginDate(validStartDate);
                pro.setUseEndDate(validEndDate);

                pro.setIssueNum(number);
                pro.setADUrl("");

                pro.setShareTitle(shareTitle);
                pro.setShareNote(shareContent);
                pro.setShareImgUrl(mShareImgUrl);

                pro.setModify_UserId(Integer.valueOf(mUser.getUserID()));

                String json = MyApp.getGson().toJson(pro);
                Log.i("Json:" + json);
                new SaveTask().execute(json);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2000://选择图片
                    mShareImgUrl = data.getStringExtra("img");

                    Glide.with(this)
                            .load(mShareImgUrl)
                            .centerCrop()
                            .crossFade()
                            //.dontAnimate()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(mShareImg);
                    break;
            }
        }
    }

    private class SaveTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = DBPubService.editStorePromotion(params[0]);
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
                success(result);
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);

            boolean isError = jsonObject.optBoolean("HasError");
            String message = jsonObject.optString("Message");

            if (!isError) {
                toast("保存成功");
                setResult(RESULT_OK);
                finish();
            } else {
                toast(message);
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }
}
