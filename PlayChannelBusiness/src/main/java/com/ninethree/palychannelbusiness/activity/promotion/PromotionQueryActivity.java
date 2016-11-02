package com.ninethree.palychannelbusiness.activity.promotion;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.bean.Promotion;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

public class PromotionQueryActivity extends BaseActivity {

    private Promotion mPromotion;

    private TextView mActivityName;
    private TextView mActivityContent;
    private TextView mActivityType;

    private TextView mPrice;

    private LinearLayout mPduLayout;
    private TextView mPduName;
    private ImageView mPduImg;

    private TextView mNumber;

    private TextView mActivityDate;
    private TextView mValidDate;

    private TextView mShareTitle;
    private TextView mShareContent;
    private ImageView mShareImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("促销活动信息");

        initView();

        mPromotion = (Promotion) getIntent().getSerializableExtra("promotion");

        if (mPromotion.getStatus() != 0){

        }

        if (mPromotion.getPromotionType() == 1){//产品
            mPduName.setText(mPromotion.getDataTitle());
            Glide.with(this)
                    .load(mPromotion.getDataLogo())
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mPduImg);
            mPduLayout.setVisibility(View.VISIBLE);
        }

        mActivityName.setText(mPromotion.getTitle());
        mActivityContent.setText(mPromotion.getNote());
        mActivityType.setText(mPromotion.getPromotionType() == 0?"面值卡":"产品");

        mPrice.setText(mPromotion.getPrice() + "元");

        mNumber.setText(mPromotion.getIssueNum()+"");
        mActivityDate.setText(mPromotion.getPromotionBeginDate().substring(0,10) +" - "+ mPromotion.getPromotionEndDate().substring(0,10));
        mValidDate.setText(mPromotion.getUseBeginDate().substring(0,10) +" - "+ mPromotion.getUseEndDate().substring(0,10));

        mShareTitle.setText(mPromotion.getShareTitle());
        mShareContent.setText(mPromotion.getShareNote());

        Glide.with(this)
                .load(mPromotion.getShareImgUrl())
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(mShareImg);
    }

    private void initView() {
        mActivityName = (TextView) findViewById(R.id.activity_name);
        mActivityContent = (TextView) findViewById(R.id.activity_content);
        mActivityType = (TextView) findViewById(R.id.activity_type);

        mPrice = (TextView) findViewById(R.id.price);

        mPduLayout = (LinearLayout) findViewById(R.id.ll_pdu);
        mPduName = (TextView) findViewById(R.id.pduName);
        mPduImg = (ImageView) findViewById(R.id.pduImg);

        mNumber = (TextView) findViewById(R.id.number);
        mActivityDate = (TextView) findViewById(R.id.activity_date);
        mValidDate = (TextView) findViewById(R.id.valid_date);

        mShareTitle = (TextView) findViewById(R.id.share_title);
        mShareContent = (TextView) findViewById(R.id.share_content);
        mShareImg = (ImageView) findViewById(R.id.share_img);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_promotion_query);
    }

    @Override
    public void onClick(View v) {

    }


}
