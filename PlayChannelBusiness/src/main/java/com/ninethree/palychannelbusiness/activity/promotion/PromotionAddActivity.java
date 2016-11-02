package com.ninethree.palychannelbusiness.activity.promotion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.activity.PduListActivity;
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
 * 新增促销
 */
public class PromotionAddActivity extends BaseActivity {

    private RadioGroup mRadioGroup;

    private LinearLayout mCardLayout;
    private LinearLayout mPduLayout;

    private EditText mActivityNameEt;
    private EditText mActivityContentEt;
    private TextView mPduPriceTv;
    private EditText mNumberEt;

    private EditText mCardPriceEt;
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

    private Org mOrg;
    private User mUser;

    private int type = 0;//0=面值卡，1=产品

    private String mShareImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("新增促销");
        mRightBtn.setImageResource(R.drawable.icon_save);
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setOnClickListener(this);

        initView();

        mOrg = (Org) getIntent().getSerializableExtra("org");
        mUser = (User) getIntent().getSerializableExtra("user");

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_card:
                        mCardLayout.setVisibility(View.VISIBLE);
                        mPduLayout.setVisibility(View.GONE);
                        type = 0;
                        break;
                    case R.id.rb_pdu:
                        Intent intent = new Intent(getApplicationContext(), PduListActivity.class);
                        intent.putExtra("from", "promotion");
                        intent.putExtra("org", mOrg);
                        startActivityForResult(intent, 1000);
                        mPduLayout.setVisibility(View.VISIBLE);
                        mCardLayout.setVisibility(View.GONE);
                        type = 1;
                        break;
                }
            }
        });

        mAcStartDate.setText(DateUtil.getDate("yyyy-MM-dd"));
        mAcEndDate.setText(DateUtil.afterNDay(15));
        mValidStartDate.setText(DateUtil.getDate("yyyy-MM-dd"));
        mValidEndDate.setText(DateUtil.afterNDay(15));

        mActivityNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    mShareTitle.setText(mActivityNameEt.getText().toString());
                }
            }
        });

        mActivityContentEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    mShareContent.setText(mActivityContentEt.getText().toString());
                }
            }
        });

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_promotion_add);
    }

    private void initView() {
        mCardLayout = (LinearLayout) findViewById(R.id.ll_card);
        mPduLayout = (LinearLayout) findViewById(R.id.ll_pdu);

        mRadioGroup = (RadioGroup) findViewById(R.id.rg_type);

        mActivityNameEt = (EditText) findViewById(R.id.activity_name);
        mActivityContentEt = (EditText) findViewById(R.id.activity_content);
        mCardPriceEt = (EditText) findViewById(R.id.card_price);
        mNumberEt = (EditText) findViewById(R.id.number);

        mPduNameTv = (TextView) findViewById(R.id.pduName);
        mPduNameTv.setOnClickListener(this);
        mPduImg = (ImageView) findViewById(R.id.pduImg);
        mPduPriceTv = (TextView) findViewById(R.id.pdu_price);

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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.pduName://选择产品
                intent = new Intent(this, PduListActivity.class);
                intent.putExtra("from", "promotion");
                intent.putExtra("org", mOrg);
                startActivityForResult(intent, 1000);
                break;
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
                intent = new Intent(this,ShareImgsActivity.class);
                startActivityForResult(intent,2000);
                break;
            case R.id.title_right_btn://添加

                String activityName = mActivityNameEt.getText().toString();

                if (StringUtil.isNullOrEmpty(activityName)) {
                    toast("请输入活动名称");
                    return;
                }

                String activityContent = mActivityContentEt.getText().toString();

                float price = 0;
                String dataTitle = "";
                String dataLogo = "";
                int dataId = 0;
                if (type == 0) {
                    String priceStr = mCardPriceEt.getText().toString();
                    if (StringUtil.isNullOrEmpty(priceStr)) {
                        toast("请输入面值金额");
                        return;
                    }
                    price = Float.valueOf(priceStr);
                    if (price < 1 || price > 99) {
                        toast("请输入1~99元面值金额");
                        return;
                    }
                    dataTitle = price + "元面值卡";
                } else {
                    if (mProduct == null) {
                        toast("请选择产品");
                        return;
                    }
                    dataId = mProduct.getPduId();
                    price = mProduct.getPriceFinal();
                    dataLogo = mProduct.getLogoUrl();
                    dataTitle = mProduct.getPduName();
                }

                String numberStr = mNumberEt.getText().toString();
                if (StringUtil.isNullOrEmpty(numberStr)) {
                    toast("请输入发行数量");
                    return;
                }
                int number = Integer.valueOf(numberStr);
                if (number < 1){
                    toast("发行数量必须大于0");
                    return;
                }

                if (mShareImgUrl == null){
                    toast("请选择分享图片");
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
                pro.setPromotionType(type);
                pro.setDataId(dataId);
                pro.setDataTitle(dataTitle);
                pro.setDataLogo(dataLogo);

                pro.setOrgId(Integer.valueOf(mOrg.getOrgId()));
                pro.setOrgGuid(mOrg.getOrgGuid());

                pro.setTitle(activityName);
                pro.setNote(activityContent);
                pro.setPrice(price);

                pro.setPromotionBeginDate(acStartDate);
                pro.setPromotionEndDate(acEndDate);
                pro.setUseBeginDate(validStartDate);
                pro.setUseEndDate(validEndDate);

                pro.setIssueNum(number);
                pro.setADUrl("");

                pro.setShareTitle(shareTitle);
                pro.setShareNote(shareContent);
                pro.setShareImgUrl(mShareImgUrl);

                pro.setCreate_UserId(Integer.valueOf(mUser.getUserID()));

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
                case 1000://选择产品
                    mProduct = (Product) data.getSerializableExtra("product");
                    Log.i("选择的产品：" + mProduct.getPduName());
                    mPduNameTv.setText(mProduct.getPduName());

                    Glide.with(this)
                            .load(mProduct.getLogoUrl())
                            .centerCrop()
                            .crossFade()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(mPduImg);

                    mPduPriceTv.setText(mProduct.getPriceFinal() + "元");

                    break;
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

            SoapObject result = DBPubService.addStorePromotion(params[0]);
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
